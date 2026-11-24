package com.forged.forged_api.seed

import com.forged.forged_api.entity.Clase
import com.forged.forged_api.entity.Disponibilidad
import com.forged.forged_api.entity.EjercicioRutina
import com.forged.forged_api.entity.Entrenador
import com.forged.forged_api.entity.Membresia
import com.forged.forged_api.entity.Pago
import com.forged.forged_api.entity.Reserva
import com.forged.forged_api.entity.Rutina
import com.forged.forged_api.entity.Usuario
import com.forged.forged_api.repository.ClaseRepository
import com.forged.forged_api.repository.DisponibilidadRepository
import com.forged.forged_api.repository.EjercicioRutinaRepository
import com.forged.forged_api.repository.EntrenadorRepository
import com.forged.forged_api.repository.MembresiaRepository
import com.forged.forged_api.repository.PagoRepository
import com.forged.forged_api.repository.ReservaRepository
import com.forged.forged_api.repository.RutinaRepository
import com.forged.forged_api.repository.UsuarioRepository
import com.forged.forged_api.security.PasswordHasher
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataSeeder {

    @Bean
    fun seed(
        usuarios: UsuarioRepository,
        entrenadores: EntrenadorRepository,
        clases: ClaseRepository,
        reservas: ReservaRepository,
        membresias: MembresiaRepository,
        rutinas: RutinaRepository,
        ejercicios: EjercicioRutinaRepository,
        pagos: PagoRepository,
        disponibilidad: DisponibilidadRepository
    ) = CommandLineRunner {
        if (usuarios.count() == 0L) {
            usuarios.save(
                Usuario(
                    email = "carlos@mail.com",
                    nombre = "Carlos",
                    password = PasswordHasher.hash("carlos@mail.com", "1234")
                )
            )
        }

        if (entrenadores.count() == 0L) {
            entrenadores.save(
                Entrenador(
                    nombre = "Carlos Arturo",
                    rol = "Entrenador Personal",
                    calificacion = 4.9,
                    resenas = 180,
                    especialidades = "Musculación,Resistencia",
                    sobreMi = "Soy entrenador certificado con más de 5 años de experiencia ayudando a personas a alcanzar su objetivo."
                )
            )
            entrenadores.save(
                Entrenador(
                    nombre = "Laura Gomez",
                    rol = "Entrenadora Personal",
                    calificacion = 4.8,
                    resenas = 150,
                    especialidades = "Pilates,Hipertrofia",
                    sobreMi = "Especialista en entrenamiento funcional y tonificación."
                )
            )
            entrenadores.save(
                Entrenador(
                    nombre = "Alejandro Correa",
                    rol = "Entrenador Personal",
                    calificacion = 4.7,
                    resenas = 120,
                    especialidades = "Funcional,Cardio",
                    sobreMi = "Te acompaño en entrenamientos funcionales para mejorar tu condición física."
                )
            )
        }

        if (clases.count() == 0L) {
            clases.save(
                Clase(
                    nombre = "Spinning", dia = "Lunes", horario = "6:00 pm - 7:00 pm",
                    entrenador = "Carlos Arturo", cuposOcupados = 8, cuposTotal = 15,
                    duracionMin = 60, descripcion = "Mejora tu resistencia y quema calorias"
                )
            )
            clases.save(
                Clase(
                    nombre = "Yoga", dia = "Martes", horario = "7:00 pm - 8:00 pm",
                    entrenador = "Laura Gomez", cuposOcupados = 12, cuposTotal = 15,
                    duracionMin = 60, descripcion = "Mejora tu flexibilidad y postura"
                )
            )
            clases.save(
                Clase(
                    nombre = "Entrenamiento funcional", dia = "Viernes", horario = "6:00 pm - 7:00 pm",
                    entrenador = "Alejandro Correa", cuposOcupados = 9, cuposTotal = 15,
                    duracionMin = 60, descripcion = "Trabaja tu cuerpo completo con ejercicios funcionales"
                )
            )
        }

        if (reservas.count() == 0L) {
            val spinning = clases.findAllByOrderByIdAsc().firstOrNull()
            reservas.save(
                Reserva(
                    idClase = spinning?.id,
                    titulo = "Spinning",
                    fecha = "mie, 18 jun",
                    hora = "6:00 pm",
                    entrenador = "Carlos Arturo",
                    duracion = "60 min",
                    estado = "Activa",
                    esGrupal = true,
                    emailUsuario = "carlos@mail.com"
                )
            )
        }

        if (membresias.count() == 0L) {
            membresias.save(
                Membresia(
                    tipoPlan = "Plan premium",
                    duracion = "1 mes",
                    costo = "$25.000",
                    fechaInicio = "25 sep 2026",
                    fechaVencimiento = "25 oct 2026",
                    metodoPago = "****4312",
                    emailUsuario = "carlos@mail.com"
                )
            )
        }

        if (rutinas.count() == 0L) {
            rutinas.save(
                Rutina(
                    nombre = "FULL BODY",
                    tipo = "Fuerza",
                    nivel = "Intermedio",
                    duracionMin = 45,
                    numeroEjercicios = 6,
                    detalle = "4 series - 12 repeticiones",
                    subtitulo = ""
                )
            )
            rutinas.save(
                Rutina(
                    nombre = "LOWER BODY",
                    tipo = "Hipertrofia",
                    nivel = "Intermedio",
                    duracionMin = 50,
                    numeroEjercicios = 6,
                    detalle = "4 series - 12 repeticiones",
                    subtitulo = ""
                )
            )
            rutinas.save(
                Rutina(
                    nombre = "LOWER BODY",
                    tipo = "Resistencia",
                    nivel = "Avanzado",
                    duracionMin = 30,
                    numeroEjercicios = 7,
                    detalle = "7x4 Challenge",
                    subtitulo = "30 Days"
                )
            )
        }

        if (ejercicios.count() == 0L) {
            val primeraRutina = rutinas.findAllByOrderByIdAsc().firstOrNull()?.id ?: 1L
            ejercicios.save(
                EjercicioRutina(
                    idRutina = primeraRutina,
                    nombre = "Warming Up",
                    tiempo = "02:00",
                    completado = true
                )
            )
            repeat(4) {
                ejercicios.save(
                    EjercicioRutina(
                        idRutina = primeraRutina,
                        nombre = "Traditional Job",
                        tiempo = "00:20",
                        completado = false
                    )
                )
            }
            ejercicios.save(
                EjercicioRutina(
                    idRutina = primeraRutina,
                    nombre = "Traditional Job",
                    tiempo = "",
                    completado = false
                )
            )
        }

        if (pagos.count() == 0L) {
            pagos.save(
                Pago(
                    plan = "Plan premium",
                    monto = "$25.000",
                    fechaInicio = "25 sep 2026",
                    fechaVencimiento = "25 oct 2026",
                    metodoPago = "****4312",
                    estado = "Pagado",
                    emailUsuario = "carlos@mail.com"
                )
            )
        }

        if (disponibilidad.count() == 0L) {
            val horas = listOf("09:00", "10:00", "11:00", "12:00", "01:00", "2:00", "3:00", "4:00", "5:00")
            horas.forEach { hora ->
                disponibilidad.save(
                    Disponibilidad(
                        idEntrenador = 1,
                        entrenador = "Carlos Arturo",
                        dia = "18",
                        hora = hora,
                        disponible = true
                    )
                )
            }
        }
    }
}
