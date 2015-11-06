- bolja se pokazala implementacija u kojoj se koristi i k-turnirska i slucajna selekcija (pa je ta i implementirana)
- kada se koristi za oba roditelja k-turnirska selekcija selekcijski pritisak je prevelik (proporcionalan s k) te algoritam uspjeva u manjoj mjeri
- uvodenjem slucajne selekcije smanjuje se selekcijski pritisak te algoritam uspjeva u vecoj mjeri naci optimalno rjesenje, ne previse ovisno o k

- za operator krizanja koristeno je uniformno krizanje
- najbolje rjesenje se pokazalo ono koje ne koristi operator mutacije
- comparison factor je postavljen fiksno na 0 (opcija koja koristi varijabilni comparison factor (comp_factor = max(f(x))) se pokazala losijom)

MAX_EFFORT = 1000; // radi ljepseg grafa :) ali i brze
MAX_SIZE = 1000; // empirijski odredeno
MIN_SIZE = 2; // ispod nema smisla, iznad ima
K_TOURNAMENT_CONSTANT = 2; // prokomentirano vec gore kako se ne primjete razlike ovisno o ovom faktoru
