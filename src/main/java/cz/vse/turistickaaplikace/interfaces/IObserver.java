package cz.vse.turistickaaplikace.interfaces;

/*******************************************************************************
 * Instance interfejsu {@code IObserver} má na starosti aktualizace
 * stavu World
 *
 * @author  Konstantin PANKRATOV
 * @version 2023-Winter
 */
public interface IObserver {
    /**
     * Metoda pro aktualizaci stavu hry.
     */
    void aktualizuj();
}
