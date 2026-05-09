import { useEffect, useState } from "react";
import { Link } from "react-router";
import { Plus, Check, X, AlertCircle } from "lucide-react";
import { Table } from "../../components/ui/Table";
import { Button } from "../../components/ui/Button";
import { StatusBadge } from "../../components/ui/StatusBadge";
import { Reserva, EstadoReserva } from "../../types";
import { hotelService } from "../../services/hotelService";

export function ReservasList() {
    const [reservas, setReservas] = useState<Reserva[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const cargarReservas = async () => {
        try {
            setLoading(true);
            setError("");
            setReservas(await hotelService.listarReservas());
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error cargando reservas");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { cargarReservas(); }, []);

    const columns = [
        { key: "codigoReserva", label: "Código" },
        { key: "huesped", label: "Huésped", render: (r: Reserva) => `${r.huesped?.nombre ?? ""} ${r.huesped?.apellido ?? ""}` },
        { key: "numeroHabitacion", label: "Habitación" },
        { key: "fechas", label: "Fechas", render: (r: Reserva) => `${r.fechaInicio} → ${r.fechaFin}` },
        { key: "personas", label: "Personas", render: (r: Reserva) => `${r.adultos} adultos, ${r.ninos} niños` },
        { key: "estado", label: "Estado", render: (r: Reserva) => <StatusBadge status={r.estado} type="reserva" /> },
    ];

    const ejecutar = async (accion: () => Promise<Reserva>) => {
        try { setError(""); await accion(); await cargarReservas(); }
        catch (err) { setError(err instanceof Error ? err.message : "Error actualizando reserva"); }
    };

    return (
        <div className="p-8">
            <div className="flex items-center justify-between mb-8">
                <div><h1 className="text-3xl font-bold text-gray-900">Reservas</h1><p className="text-gray-500 mt-1">Gestión de todas las reservas del hotel</p></div>
                <Link to="/reservas/nueva"><Button variant="primary" size="lg"><div className="flex items-center gap-2"><Plus className="w-5 h-5" />Nueva Reserva</div></Button></Link>
            </div>
            {error && <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">{error}</div>}
            {loading ? <p className="text-gray-500">Cargando reservas...</p> : <Table data={reservas} columns={columns} searchPlaceholder="Buscar por huésped, habitación..." actions={(reserva) => <>
                {reserva.estado === EstadoReserva.RESERVADA && <Button variant="success" size="sm" onClick={() => ejecutar(() => hotelService.confirmarReserva(reserva.id))}><Check className="w-4 h-4" /></Button>}
                {(reserva.estado === EstadoReserva.RESERVADA || reserva.estado === EstadoReserva.CONFIRMADA) && <>
                    <Button variant="danger" size="sm" onClick={() => ejecutar(() => hotelService.cancelarReserva(reserva.id))}><X className="w-4 h-4" /></Button>
                    <Button variant="secondary" size="sm" onClick={() => ejecutar(() => hotelService.noShowReserva(reserva.id))}><AlertCircle className="w-4 h-4" /></Button>
                </>}
            </>} />}
        </div>
    );
}
