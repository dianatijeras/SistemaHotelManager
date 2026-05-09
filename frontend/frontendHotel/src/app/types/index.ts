export enum EstadoReserva {
    RESERVADA = 'RESERVADA',
    CONFIRMADA = 'CONFIRMADA',
    CANCELADA = 'CANCELADA',
    NO_SHOW = 'NO_SHOW',
    CHECKED_IN = 'CHECKED_IN',
    FINALIZADA = 'FINALIZADA'
}

export enum EstadoHabitacion {
    DISPONIBLE = 'DISPONIBLE',
    RESERVADA = 'RESERVADA',
    OCUPADA = 'OCUPADA',
    EN_LIMPIEZA = 'EN_LIMPIEZA',
    MANTENIMIENTO = 'MANTENIMIENTO',
    FUERA_DE_SERVICIO = 'FUERA_DE_SERVICIO'
}

export enum EstadoPago {
    PENDIENTE = 'PENDIENTE',
    PAGADO = 'PAGADO',
    RECHAZADO = 'RECHAZADO',
    REEMBOLSADO = 'REEMBOLSADO'
}

export enum MetodoPago {
    TARJETA_DEBITO = 'TARJETA_DEBITO',
    TARJETA_CREDITO = 'TARJETA_CREDITO',
    TRANSFERENCIA = 'TRANSFERENCIA',
    EFECTIVO = 'EFECTIVO',
    PSE = 'PSE'
}

export enum TipoHabitacion {
    ESTANDAR = 'ESTANDAR',
    SUPERIOR = 'SUPERIOR',
    DELUXE = 'DELUXE',
    SUITE = 'SUITE'
}

export interface Huesped {
    id: string;
    nombre: string;
    apellido: string;
    documento?: string;
    email: string;
    telefono: string;
    username?: string;
}

export interface Habitacion {
    id: string;
    numero: string;
    tipo: TipoHabitacion;
    capacidad: number;
    precioNoche: number;
    estado: EstadoHabitacion;
    descripcion?: string;
    piso?: number;
}

export interface Reserva {
    id: string;
    codigoReserva: string;
    idHuesped: string;
    numeroHabitacion: string;
    fechaInicio: string;
    fechaFin: string;
    adultos: number;
    ninos: number;
    estado: EstadoReserva;
    huesped?: Huesped;
    habitacion?: Habitacion;
}

export interface Consumo {
    id: string;
    descripcion: string;
    cantidad: number;
    precioUnitario: number;
    total: number;
    fecha?: string;
}

export interface Estadia {
    id: string;
    idReserva: string;
    fechaInicio: string;
    fechaFin: string | null;
    totalHabitacion: number;
    totalConsumos: number;
    totalFinal: number;
    estado: string;
    reserva?: Reserva;
    consumos?: Consumo[];
}

export interface Pago {
    id: string;
    idEstadia: string;
    monto: number;
    metodoPago: MetodoPago;
    referencia: string;
    estado: EstadoPago;
    fecha: string;
}


export interface DashboardStats {
    totalHabitaciones: number;
    habitacionesDisponibles: number;
    habitacionesOcupadas: number;
    habitacionesEnLimpieza: number;
    totalHuespedes: number;
    totalReservas: number;
    reservasActivas: number;
    reservasEnProceso: number;
    estadiasEnCurso: number;
    pagosPendientes: number;
    totalPagado: number;
    checkoutsHoy: number;
    totalUsuarios: number;
    totalRecepcionistas: number;
    totalPersonalLimpieza: number;
}
