import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { ArrowLeft } from "lucide-react";
import { Card } from "../../components/ui/Card";
import { Button } from "../../components/ui/Button";
import { Habitacion, Huesped } from "../../types";
import { hotelService } from "../../services/hotelService";

/**
 * Componente para crear una nueva reserva en el hotel. Permite seleccionar un huésped, una habitación disponible, fechas de estadía y número de personas para generar una nueva reserva.
 * @constructor
 */
export function ReservasNueva() {
    const navigate = useNavigate();
    const [huespedes, setHuespedes] = useState<Huesped[]>([]);
    const [habitacionesDisponibles, setHabitacionesDisponibles] = useState<Habitacion[]>([]);
    const [error, setError] = useState("");
    const [formData, setFormData] = useState({ idHuesped: "", numeroHabitacion: "", fechaInicio: "", fechaFin: "", adultos: 1, ninos: 0 });

    useEffect(() => {
        Promise.all([hotelService.listarHuespedes(), hotelService.listarHabitacionesDisponibles()])
            .then(([h, hab]) => { setHuespedes(h); setHabitacionesDisponibles(hab); })
            .catch((err) => setError(err instanceof Error ? err.message : "Error cargando datos"));
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setError("");
            await hotelService.crearReserva({ ...formData, numeroHabitacion: Number(formData.numeroHabitacion) });
            navigate("/reservas");
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error creando reserva");
        }
    };

    return <div className="p-8">
        <Button variant="secondary" size="sm" onClick={() => navigate(-1)}><ArrowLeft className="w-4 h-4 mr-2" />Volver</Button>
        <div className="mt-4 mb-8"><h1 className="text-3xl font-bold text-gray-900">Nueva Reserva</h1><p className="text-gray-500 mt-1">Crear una nueva reserva</p></div>
        {error && <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">{error}</div>}
        <Card><form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-2 gap-4">
                <div><label className="block text-sm font-medium text-gray-700 mb-2">Huésped *</label><select value={formData.idHuesped} onChange={(e) => setFormData({ ...formData, idHuesped: e.target.value })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" required><option value="">Seleccione...</option>{huespedes.map((h) => <option key={h.id} value={h.id}>{h.nombre} {h.apellido}</option>)}</select></div>
                <div><label className="block text-sm font-medium text-gray-700 mb-2">Habitación *</label><select value={formData.numeroHabitacion} onChange={(e) => setFormData({ ...formData, numeroHabitacion: e.target.value })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" required><option value="">Seleccione...</option>{habitacionesDisponibles.map((h) => <option key={h.id} value={h.numero}>Hab. {h.numero} - {h.tipo} - ${h.precioNoche}</option>)}</select></div>
            </div>
            <div className="grid grid-cols-2 gap-4">
                <div><label className="block text-sm font-medium text-gray-700 mb-2">Fecha inicio *</label><input type="date" value={formData.fechaInicio} onChange={(e) => setFormData({ ...formData, fechaInicio: e.target.value })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" required /></div>
                <div><label className="block text-sm font-medium text-gray-700 mb-2">Fecha fin *</label><input type="date" value={formData.fechaFin} onChange={(e) => setFormData({ ...formData, fechaFin: e.target.value })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" required /></div>
            </div>
            <div className="grid grid-cols-2 gap-4">
                <div><label className="block text-sm font-medium text-gray-700 mb-2">Adultos *</label><input type="number" min="1" value={formData.adultos} onChange={(e) => setFormData({ ...formData, adultos: Number(e.target.value) })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" required /></div>
                <div><label className="block text-sm font-medium text-gray-700 mb-2">Niños</label><input type="number" min="0" value={formData.ninos} onChange={(e) => setFormData({ ...formData, ninos: Number(e.target.value) })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" /></div>
            </div>
            <div className="flex justify-end gap-3"><Button type="button" variant="secondary" onClick={() => navigate("/reservas")}>Cancelar</Button><Button type="submit" variant="primary">Crear Reserva</Button></div>
        </form></Card>
    </div>;
}
