package uf3_pvp_ex1.server;

public class ReservationException extends Exception {
    private int remainingSeats;

    public ReservationException(String errorMessage, int remainingSeats) {
        super(errorMessage);
        this.remainingSeats = remainingSeats;
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }
}
