1. zadatak

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


2. zadatak

- koristena je metoda krizanja u kojoj se uzima podniz iz jednog roditelja te se ostatak kromosoma prepise iz drugog roditelja (pazeci naravno na zadrzavanje svojstava permutacije)
- empirijski se pokazala dobrom metoda mutacije gdje se u svakom djetetu napravi izmedu 0 i 2 mutacije
- prilikom iteriranja SASEGASA algoritma, najboljom se pokazao operator spajanja koji u svakom koraku spoji dva susjedna "sela"
- isto tako empirijski su odredeni slijedeci parametri:
SUCCESS_RATIO = 0.5;
MAX_SELECTION_PRESSURE = 20;
NUMBER_OF_ITERATIONS = 50;
K_TOURNAMENT_CONSTANT = 3;
MAX_VILLAGE_COUNT = 16;
POPULATION_COUNT = 400 * MAX_VILLAGE_COUNT;