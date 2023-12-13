package cz.vse.turistickaaplikace.interfaces;

import cz.vse.turistickaaplikace.enumerators.AppChange;

/*******************************************************************************
 * Instance interfejsu {@code IObservable} má na starosti předávání
 * stavu World observeru
 *
 * @author  Konstantin PANKRATOV
 * @version 2023-Winter
 */
public interface IObservable {
    /**
     * Metoda pro registraci pozorovatelů (observerů) k pozorování konkrétních událostí ve hře.
     *
     * @param changeType Typ události, na kterou se pozorovatel registroval.
     * @param observer    Pozorovatel (observer), který má být registrován pro sledování události.
     */
    void registruj(AppChange changeType, IObserver observer);
}
