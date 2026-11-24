param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"
$script:fallos = 0

function Ok([string]$mensaje) {
    Write-Host "[OK]    $mensaje" -ForegroundColor Green
}

function Fallo([string]$mensaje) {
    Write-Host "[FALLO] $mensaje" -ForegroundColor Red
    $script:fallos++
}

function Probar([string]$nombre, [scriptblock]$accion) {
    try {
        & $accion
        Ok $nombre
    } catch {
        Fallo "$nombre -> $($_.Exception.Message)"
    }
}

function Post-Json([string]$ruta, $datos) {
    $json = $datos | ConvertTo-Json -Compress
    return Invoke-RestMethod -Method Post -Uri "$BaseUrl$ruta" `
        -ContentType "application/json" -Body $json
}

Write-Host ""
Write-Host "Probando la API Forged en $BaseUrl" -ForegroundColor Cyan
Write-Host "----------------------------------------" -ForegroundColor DarkGray

# Verificación inicial: ¿está levantada la API?
try {
    Invoke-RestMethod "$BaseUrl/clases" -TimeoutSec 5 | Out-Null
} catch {
    Write-Host ""
    Write-Host "No se pudo conectar con la API." -ForegroundColor Red
    Write-Host "Verifica que MySQL esté iniciado y que la API esté corriendo con:" -ForegroundColor Yellow
    Write-Host '  cd "C:\Users\MI PC\Documents\forged\forged"' -ForegroundColor Yellow
    Write-Host "  .\gradlew bootRun" -ForegroundColor Yellow
    exit 1
}

# 1. Clases
Probar "GET /clases" {
    $clases = Invoke-RestMethod "$BaseUrl/clases"
    if (@($clases).Count -eq 0) { throw "La API no devolvió clases" }
    Write-Host "        Clases encontradas: $(@($clases).Count)" -ForegroundColor DarkGray
}

# 2. Login correcto
Probar "POST /auth/login con carlos@mail.com / 1234" {
    $usuario = Post-Json "/auth/login" @{ email = "carlos@mail.com"; password = "1234" }
    if ($usuario.email -ne "carlos@mail.com") { throw "Correo inesperado: $($usuario.email)" }
}

# 3. Login incorrecto -> 401
Probar "Login con contraseña incorrecta devuelve 401" {
    try {
        Post-Json "/auth/login" @{ email = "carlos@mail.com"; password = "incorrecta" } | Out-Null
        throw "La API aceptó una contraseña incorrecta"
    } catch {
        $codigo = $_.Exception.Response.StatusCode.value__
        if ($codigo -ne 401) { throw "Se esperaba 401 y llegó $codigo" }
    }
}

# 4. Registro duplicado -> 409
Probar "Registro con correo existente devuelve 409" {
    try {
        Post-Json "/auth/registro" @{ nombre = "Carlos"; email = "carlos@mail.com"; password = "1234" } | Out-Null
        throw "La API permitió registrar un correo duplicado"
    } catch {
        $codigo = $_.Exception.Response.StatusCode.value__
        if ($codigo -ne 409) { throw "Se esperaba 409 y llegó $codigo" }
    }
}

# 5. Recuperar contraseña
Probar "GET /auth/recuperar" {
    $existe = Invoke-RestMethod "$BaseUrl/auth/recuperar?email=carlos@mail.com"
    if (-not $existe.existe) { throw "No reconoció el correo registrado" }
}

# 6. Usuario por correo
Probar "GET /auth/usuario" {
    $usuario = Invoke-RestMethod "$BaseUrl/auth/usuario?email=carlos@mail.com"
    if ($usuario.nombre -ne "Carlos") { throw "Nombre inesperado: $($usuario.nombre)" }
}

# 7. Entrenadores
Probar "GET /entrenadores y /entrenadores/primero" {
    $entrenadores = Invoke-RestMethod "$BaseUrl/entrenadores"
    if (@($entrenadores).Count -eq 0) { throw "No devolvió entrenadores" }
    $primero = Invoke-RestMethod "$BaseUrl/entrenadores/primero"
    if ([string]::IsNullOrWhiteSpace($primero.nombre)) { throw "El primer entrenador no tiene nombre" }
    Write-Host "        Entrenadores encontrados: $(@($entrenadores).Count)" -ForegroundColor DarkGray
}

# 8. Reservas del usuario
Probar "GET /reservas?email=" {
    $reservas = Invoke-RestMethod "$BaseUrl/reservas?email=carlos@mail.com"
    Write-Host "        Reservas del usuario: $(@($reservas).Count)" -ForegroundColor DarkGray
}

# 9. Membresía actual
Probar "GET /membresias/actual?email=" {
    $membresia = Invoke-RestMethod "$BaseUrl/membresias/actual?email=carlos@mail.com"
    if ([string]::IsNullOrWhiteSpace($membresia.tipoPlan)) { throw "La membresía no tiene tipo de plan" }
}

# 10. Crear reserva, validar cupo y cancelarla
Probar "POST /reservas y DELETE /reservas/{id} actualizan cupos" {
    $clase = Invoke-RestMethod "$BaseUrl/clases/1"
    $cuposAntes = [int]$clase.cuposOcupados

    $reserva = Post-Json "/reservas" @{
        idClase     = 1
        titulo      = "Spinning"
        fecha       = "prueba"
        hora        = "6:00 pm"
        entrenador  = "Carlos Arturo"
        duracion    = "60 min"
        esGrupal    = $true
        emailUsuario = "carlos@mail.com"
    }

    try {
        $cuposDespues = [int](Invoke-RestMethod "$BaseUrl/clases/1").cuposOcupados
        if ($cuposDespues -ne ($cuposAntes + 1)) {
            throw "El cupo no aumentó: antes=$cuposAntes despues=$cuposDespues"
        }
    } finally {
        Invoke-WebRequest -Method Delete -Uri "$BaseUrl/reservas/$($reserva.id)" -UseBasicParsing | Out-Null
    }

    $cuposFinal = [int](Invoke-RestMethod "$BaseUrl/clases/1").cuposOcupados
    if ($cuposFinal -ne $cuposAntes) {
        throw "El cupo no volvió a su valor: antes=$cuposAntes final=$cuposFinal"
    }
}

# 11. Disponibilidad por entrenador y día
Probar "GET /disponibilidad?idEntrenador=&dia=" {
    $franjas = Invoke-RestMethod "$BaseUrl/disponibilidad?idEntrenador=1&dia=18"
    if (@($franjas).Count -eq 0) { throw "No devolvió franjas de disponibilidad" }
    if (@($franjas)[0].idEntrenador -ne 1) { throw "La franja no pertenece al entrenador 1" }
    Write-Host "        Franjas encontradas: $(@($franjas).Count)" -ForegroundColor DarkGray
}

# 12. Rutinas
Probar "GET /rutinas y GET /rutinas/{id}" {
    $rutinas = Invoke-RestMethod "$BaseUrl/rutinas"
    if (@($rutinas).Count -eq 0) { throw "No devolvió rutinas" }
    $primera = Invoke-RestMethod "$BaseUrl/rutinas/$(@($rutinas)[0].id)"
    if ([string]::IsNullOrWhiteSpace($primera.nombre)) { throw "La rutina no tiene nombre" }
    Write-Host "        Rutinas encontradas: $(@($rutinas).Count)" -ForegroundColor DarkGray
}

# 13. Ejercicios filtrados por rutina y actualización de estado
Probar "GET /ejercicios?idRutina= y PUT /ejercicios/{id}" {
    $rutinas = Invoke-RestMethod "$BaseUrl/rutinas"
    $idRutina = @($rutinas)[0].id
    $ejercicios = Invoke-RestMethod "$BaseUrl/ejercicios?idRutina=$idRutina"
    if (@($ejercicios).Count -eq 0) { throw "No devolvió ejercicios para la rutina $idRutina" }

    $ejercicio = @($ejercicios)[0]
    $estadoOriginal = [bool]$ejercicio.completado
    $cuerpoOriginal = $ejercicio | ConvertTo-Json -Compress

    $ejercicio.completado = -not $estadoOriginal
    $cuerpoCambiado = $ejercicio | ConvertTo-Json -Compress

    try {
        $actualizado = Invoke-RestMethod -Method Put -Uri "$BaseUrl/ejercicios/$($ejercicio.id)" `
            -ContentType "application/json" -Body $cuerpoCambiado
        if ([bool]$actualizado.completado -eq $estadoOriginal) {
            throw "El estado del ejercicio no cambió"
        }
    } finally {
        Invoke-RestMethod -Method Put -Uri "$BaseUrl/ejercicios/$($ejercicio.id)" `
            -ContentType "application/json" -Body $cuerpoOriginal | Out-Null
    }
    Write-Host "        Ejercicios de la rutina 1: $(@($ejercicios).Count)" -ForegroundColor DarkGray
}

# 14. Pagos: listar, crear y eliminar
Probar "GET /pagos, POST /pagos y DELETE /pagos/{id}" {
    $pagosAntes = @((Invoke-RestMethod "$BaseUrl/pagos")).Count

    $pago = Post-Json "/pagos" @{
        plan               = "Plan de prueba"
        monto              = '$1.000'
        fechaInicio        = "20 nov 2026"
        fechaVencimiento   = "20 dic 2026"
        metodoPago         = "****0000"
        estado             = "Pagado"
    }

    try {
        $pagosDespues = @((Invoke-RestMethod "$BaseUrl/pagos")).Count
        if ($pagosDespues -ne ($pagosAntes + 1)) {
            throw "El pago no se registró: antes=$pagosAntes despues=$pagosDespues"
        }
    } finally {
        Invoke-WebRequest -Method Delete -Uri "$BaseUrl/pagos/$($pago.id)" -UseBasicParsing | Out-Null
    }

    $pagosFinal = @((Invoke-RestMethod "$BaseUrl/pagos")).Count
    if ($pagosFinal -ne $pagosAntes) {
        throw "El pago no se eliminó: antes=$pagosAntes final=$pagosFinal"
    }
}

Write-Host "----------------------------------------" -ForegroundColor DarkGray
if ($script:fallos -eq 0) {
    Write-Host "RESULTADO: todas las pruebas pasaron." -ForegroundColor Green
    exit 0
} else {
    Write-Host "RESULTADO: $($script:fallos) prueba(s) fallaron." -ForegroundColor Red
    exit 1
}
