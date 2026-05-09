import { useEffect, useState } from "react";
import { Plus, Check, X, RotateCcw } from "lucide-react";
import { Table } from "../../components/ui/Table";
import { Button } from "../../components/ui/Button";
import { Modal } from "../../components/ui/Modal";
import { StatusBadge } from "../../components/ui/StatusBadge";
import { EstadoPago, Estadia, MetodoPago, Pago } from "../../types";
import { hotelService } from "../../services/hotelService";

/**
 * Componente para listar y gestionar pagos del hotel. Permite registrar nuevos pagos asociados a estadías, así como aprobar, rechazar o reembolsar pagos existentes.
 * @constructor
 */
export function PagosList() {
    const [pagos, setPagos] = useState<Pago[]>([]);
    const [estadias, setEstadias] = useState<Estadia[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [formData, setFormData] = useState({ idEstadia: "", monto: 0, metodoPago: MetodoPago.EFECTIVO, referenciaPasarela: "" });

    const cargar = async () => {
        try {
            setLoading(true);
            setError("");
            const [pagosData, estadiasData] = await Promise.all([
                hotelService.listarPagos(),
                hotelService.listarEstadiasParaPago(),
            ]);
            setPagos(pagosData);
            setEstadias(estadiasData);
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error cargando pagos");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { cargar(); }, []);

    const columns = [
        { key: "id", label: "ID" },
        { key: "idEstadia", label: "Estadía" },
        { key: "monto", label: "Monto", render: (p: Pago) => `$${p.monto}` },
        { key: "metodoPago", label: "Método" },
        { key: "referencia", label: "Referencia" },
        { key: "estado", label: "Estado", render: (p: Pago) => <StatusBadge status={p.estado} type="pago" /> },
    ];

    const handleSeleccionarEstadia = (idEstadia: string) => {
        const estadiaSeleccionada = estadias.find((e) => e.id === idEstadia);
        setFormData({
            ...formData,
            idEstadia,
            monto: estadiaSeleccionada?.totalFinal ?? 0,
        });
    };

    const estadiaSeleccionada = estadias.find((e) => e.id === formData.idEstadia);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!formData.idEstadia) {
            setError("Debe seleccionar una estadía para registrar el pago.");
            return;
        }

        if (formData.monto <= 0) {
            setError("El monto del pago debe ser mayor a 0.");
            return;
        }

        try {
            setError("");
            await hotelService.crearPago(formData);
            setIsModalOpen(false);
            setFormData({ idEstadia: "", monto: 0, metodoPago: MetodoPago.EFECTIVO, referenciaPasarela: "" });
            await cargar();
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error registrando pago");
        }
    };

    const ejecutar = async (accion: () => Promise<Pago>) => {
        try {
            setError("");
            await accion();
            await cargar();
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error actualizando pago");
        }
    };

    return <div className="p-8">
        <div className="flex items-center justify-between mb-8"><div><h1 className="text-3xl font-bold text-gray-900">Pagos</h1><p className="text-gray-500 mt-1">Gestión de pagos y cobros</p></div><Button variant="primary" size="lg" onClick={() => setIsModalOpen(true)}><div className="flex items-center gap-2"><Plus className="w-5 h-5" />Nuevo Pago</div></Button></div>
        {error && <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">{error}</div>}
        {loading ? <p className="text-gray-500">Cargando pagos...</p> : <Table data={pagos} columns={columns} searchPlaceholder="Buscar pago..." actions={(pago) => <>
            {pago.estado === EstadoPago.PENDIENTE && <><Button variant="success" size="sm" onClick={() => ejecutar(() => hotelService.aprobarPago(pago.id, pago.referencia || "REF-MANUAL"))}><Check className="w-4 h-4" /></Button><Button variant="danger" size="sm" onClick={() => ejecutar(() => hotelService.rechazarPago(pago.id, "Rechazado manualmente"))}><X className="w-4 h-4" /></Button></>}
            {pago.estado === EstadoPago.PAGADO && <Button variant="secondary" size="sm" onClick={() => ejecutar(() => hotelService.reembolsarPago(pago.id))}><RotateCcw className="w-4 h-4" /></Button>}
        </>} />}
        <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Nuevo Pago"><form onSubmit={handleSubmit} className="space-y-4"><div><label className="block text-sm font-medium text-gray-700 mb-2">Estadía *</label><select value={formData.idEstadia} onChange={(e) => handleSeleccionarEstadia(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg" required><option value="">Seleccione estadía</option>{estadias.map((e) => <option key={e.id} value={e.id}>{e.id} - {e.reserva?.huesped?.nombre ?? "Huésped"} {e.reserva?.huesped?.apellido ?? ""} - {e.estado} - ${e.totalFinal}</option>)}</select>{estadias.length === 0 && <p className="text-xs text-gray-500 mt-2">No hay estadías en curso o finalizadas disponibles para pago. Primero realiza el check-in de una reserva.</p>}</div>{estadiaSeleccionada && <div className="bg-blue-50 p-4 rounded-lg space-y-2 text-sm"><div className="flex justify-between"><span>Valor base:</span><span>${estadiaSeleccionada.totalHabitacion}</span></div><div className="flex justify-between"><span>Total consumos:</span><span>${estadiaSeleccionada.totalConsumos}</span></div><div className="border-t border-blue-200 pt-2 flex justify-between font-bold"><span>Total final:</span><span>${estadiaSeleccionada.totalFinal}</span></div></div>}<div><label className="block text-sm font-medium text-gray-700 mb-2">Monto *</label><input type="number" min="1" value={formData.monto} readOnly className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50" required /><p className="text-xs text-gray-500 mt-1">El monto se toma del total final de la estadía: habitación + consumos.</p></div><div><label className="block text-sm font-medium text-gray-700 mb-2">Método *</label><select value={formData.metodoPago} onChange={(e) => setFormData({ ...formData, metodoPago: e.target.value as MetodoPago })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" required>{Object.values(MetodoPago).map((m) => <option key={m} value={m}>{m}</option>)}</select></div><div><label className="block text-sm font-medium text-gray-700 mb-2">Referencia</label><input value={formData.referenciaPasarela} onChange={(e) => setFormData({ ...formData, referenciaPasarela: e.target.value })} className="w-full px-4 py-2 border border-gray-300 rounded-lg" /></div><div className="flex justify-end gap-3"><Button type="button" variant="secondary" onClick={() => setIsModalOpen(false)}>Cancelar</Button><Button type="submit" variant="primary" disabled={estadias.length === 0}>Registrar</Button></div></form></Modal>
    </div>;
}
