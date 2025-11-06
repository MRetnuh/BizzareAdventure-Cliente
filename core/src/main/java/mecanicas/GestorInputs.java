package mecanicas;

import input.InputController;
import red.HiloCliente;

public class GestorInputs {

    public static void procesarInputs(
        InputController inputController,
        HiloCliente hiloCliente,
        int idJugador
    ) {
        boolean derecha = inputController.getDerecha1();
        boolean izquierda = inputController.getIzquierda1();
        boolean saltar = inputController.getSaltar1();
        boolean atacar = inputController.getAtacar1();

        // Enviar al servidor los inputs del jugador
        if (hiloCliente != null) {
            String mensaje = String.format(
                "Mover:%d:%b:%b:%b:%b",
                idJugador,
                derecha,
                izquierda,
                saltar,
                atacar
            );
            hiloCliente.sendMessage(mensaje);
        }

        // Limpiar los ataques/acciones de botón único
        if (atacar)
            inputController.setAtacarFalso1();
}
}