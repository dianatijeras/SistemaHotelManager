import { useEffect, useState } from "react";
import { Search, LogIn } from "lucide-react";
import { Card } from "../components/ui/Card";
import { Button } from "../components/ui/Button";
import { Reserva, EstadoReserva } from "../types";
import { hotelService } from "../services/hotelService";

/**
 * Componente para gestionar el proceso de check-in de huéspedes. Permite buscar reservas confirmadas o reservadas, seleccionar una reserva y registrar la llegada del huésped al hotel.
 * @constructor
 */
export function CheckIn() {
    const [busqueda, setBusqueda] = useState("");
    const [reservas, setReservas] = useState<Reserva[]>([]);
    const [reservaSeleccionada, setReservaSeleccionada] = useState<Reserva | null>(null);
    const [error, setError] = useState("");
    const [mensaje, setMensaje] = useState("");

    useEffect(() => {
        hotelService.listarReservas()
            .then((data) => setReservas(data.filter((r) => r.estado === EstadoReserva.CONFIRMADA || r.estado === EstadoReserva.RESERVADA)))
            .catch((err) => setError(err instanceof Error ? err.message : "Error cargando reservas"));
    }, []);

    const reservasFiltradas = reservas.filter((r) =>
        `${r.codigoReserva} ${r.id} ${r.huesped?.nombre ?? ""} ${r.huesped?.apellido ?? ""} ${r.numeroHabitacion}`.toLowerCase().includes(busqueda.toLowerCase())
    );

    const handleCheckIn = async () => {
        if (!reservaSeleccionada) return;
        try {
            setError("");
            await hotelService.checkIn(reservaSeleccionada.id);
            setMensaje("Check-in realizado correctamente");
            setReservaSeleccionada(null);
            setReservas((prev) => prev.filter((r) => r.id !== reservaSeleccionada.id));
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error realizando check-in");
        }
    };

    return <div className="p-8">
        <div className="mb-8"><h1 className="text-3xl font-bold text-gray-900">Check-in</h1><p className="text-gray-500 mt-1">Registrar llegada de huéspedes</p></div>
        {error && <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">{error}</div>}
        {mensaje && <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">{mensaje}</div>}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <Card><h2 className="text-lg font-semibold text-gray-900 mb-4">Buscar Reserva</h2><div className="relative mb-4"><Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" /><input type="text" value={busqueda} onChange={(e) => setBusqueda(e.target.value)} placeholder="Buscar por nombre, reserva o habitación..." className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg" /></div><div className="space-y-3">{reservasFiltradas.map((reserva) => <div key={reserva.id} onClick={() => setReservaSeleccionada(reserva)} className={`p-4 border rounded-lg cursor-pointer transition-all ${reservaSeleccionada?.id === reserva.id ? "border-blue-500 bg-blue-50" : "border-gray-200 hover:border-blue-300"}`}><div className="flex justify-between"><div><p className="font-medium text-gray-900">{reserva.huesped?.nombre} {reserva.huesped?.apellido}</p><p className="text-sm text-gray-500">Reserva {reserva.codigoReserva} - Hab. {reserva.numeroHabitacion}</p><p className="text-sm text-gray-500">{reserva.fechaInicio} → {reserva.fechaFin}</p></div><LogIn className="w-5 h-5 text-blue-600" /></div></div>)}</div></Card>
            <Card><h2 className="text-lg font-semibold text-gray-900 mb-4">Detalle del Check-in</h2>{reservaSeleccionada ? <div className="space-y-4"><div><p className="text-sm text-gray-600">Huésped</p><p className="font-medium text-gray-900">{reservaSeleccionada.huesped?.nombre} {reservaSeleccionada.huesped?.apellido}</p></div><div><p className="text-sm text-gray-600">Habitación</p><p className="font-medium text-gray-900">{reservaSeleccionada.numeroHabitacion}</p></div><div><p className="text-sm text-gray-600">Fechas</p><p className="font-medium text-gray-900">{reservaSeleccionada.fechaInicio} a {reservaSeleccionada.fechaFin}</p></div><Button variant="success" size="lg" onClick={handleCheckIn} className="w-full">Realizar Check-in</Button></div> : <p className="text-gray-500">Seleccione una reserva para continuar</p>}</Card>
        </div>
    </div>;
}
