import { useEffect, useState } from "react";
import { Link } from "react-router";
import { Search, LogOut, Eye } from "lucide-react";
import { Card } from "../components/ui/Card";
import { Button } from "../components/ui/Button";
import { Estadia } from "../types";
import { hotelService } from "../services/hotelService";

/**
 * Página de Check-out para el hotel. Permite listar las estadías activas, buscar por huésped o número de habitación, y finalizar la estadía generando el cobro correspondiente. Al seleccionar una estadía, se muestra un resumen con el total a cobrar y un enlace para ver el detalle completo de la estadía.
 * @constructor
 */
export function CheckOut() {
    const [busqueda, setBusqueda] = useState("");
    const [estadias, setEstadias] = useState<Estadia[]>([]);
    const [estadiaSeleccionada, setEstadiaSeleccionada] = useState<Estadia | null>(null);
    const [error, setError] = useState("");
    const [mensaje, setMensaje] = useState("");

    const cargar = async () => {
        try { setEstadias(await hotelService.listarEstadiasEnCurso()); }
        catch (err) { setError(err instanceof Error ? err.message : "Error cargando estadías"); }
    };
    useEffect(() => { cargar(); }, []);

    const estadiasFiltradas = estadias.filter((e) => `${e.id} ${e.reserva?.huesped?.nombre ?? ""} ${e.reserva?.huesped?.apellido ?? ""} ${e.reserva?.numeroHabitacion ?? ""}`.toLowerCase().includes(busqueda.toLowerCase()));

    const handleCheckOut = async () => {
        if (!estadiaSeleccionada) return;
        try { setError(""); await hotelService.checkOut(estadiaSeleccionada.id); setMensaje("Check-out realizado correctamente"); setEstadiaSeleccionada(null); await cargar(); }
        catch (err) { setError(err instanceof Error ? err.message : "Error realizando check-out"); }
    };

    return <div className="p-8">
        <div className="mb-8"><h1 className="text-3xl font-bold text-gray-900">Check-out</h1><p className="text-gray-500 mt-1">Finalizar estadía y generar cobro</p></div>
        {error && <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">{error}</div>}
        {mensaje && <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">{mensaje}</div>}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <Card><h2 className="text-lg font-semibold text-gray-900 mb-4">Estadías Activas</h2><div className="relative mb-4"><Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" /><input value={busqueda} onChange={(e) => setBusqueda(e.target.value)} placeholder="Buscar por nombre o habitación..." className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg" /></div><div className="space-y-3">{estadiasFiltradas.map((estadia) => <div key={estadia.id} onClick={() => setEstadiaSeleccionada(estadia)} className={`p-4 border rounded-lg cursor-pointer transition-all ${estadiaSeleccionada?.id === estadia.id ? "border-blue-500 bg-blue-50" : "border-gray-200 hover:border-blue-300"}`}><div className="flex justify-between"><div><p className="font-medium text-gray-900">{estadia.reserva?.huesped?.nombre} {estadia.reserva?.huesped?.apellido}</p><p className="text-sm text-gray-500">Estadía {estadia.id} - Hab. {estadia.reserva?.numeroHabitacion}</p><p className="text-sm text-gray-500">Total: ${estadia.totalFinal}</p></div><LogOut className="w-5 h-5 text-blue-600" /></div></div>)}</div></Card>
            <Card><h2 className="text-lg font-semibold text-gray-900 mb-4">Resumen de Check-out</h2>{estadiaSeleccionada ? <div className="space-y-4"><div><p className="text-sm text-gray-600">Huésped</p><p className="font-medium text-gray-900">{estadiaSeleccionada.reserva?.huesped?.nombre} {estadiaSeleccionada.reserva?.huesped?.apellido}</p></div><div className="bg-blue-50 p-4 rounded-lg space-y-2"><div className="flex justify-between"><span>Habitación:</span><span>${estadiaSeleccionada.totalHabitacion}</span></div><div className="flex justify-between"><span>Consumos:</span><span>${estadiaSeleccionada.totalConsumos}</span></div><div className="border-t border-blue-200 pt-2 flex justify-between font-bold"><span>Total Final:</span><span>${estadiaSeleccionada.totalFinal}</span></div></div><div className="flex gap-3"><Link to={`/estadias/${estadiaSeleccionada.id}`} className="flex-1"><Button variant="secondary" className="w-full"><Eye className="w-4 h-4 mr-2" />Ver Detalle</Button></Link><Button variant="primary" onClick={handleCheckOut} className="flex-1">Finalizar</Button></div></div> : <p className="text-gray-500">Seleccione una estadía para continuar</p>}</Card>
        </div>
    </div>;
}
