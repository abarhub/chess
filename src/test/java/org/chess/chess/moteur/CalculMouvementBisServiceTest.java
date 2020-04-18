package org.chess.chess.moteur;

import org.chess.chess.domain.*;
import org.chess.chess.notation.NotationFEN;
import org.chess.chess.service.InformationPartieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CalculMouvementBisServiceTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalculMouvementBisServiceTest.class);

    private CalculMouvementBisService calculMouvementBisService;

    private NotationFEN notationFEN = new NotationFEN();

    private InformationPartieService informationPartieService=new InformationPartieService();

    @BeforeEach
    void setUp() {
        calculMouvementBisService=new CalculMouvementBisService();
        ReflectionTestUtils.setField(notationFEN, "informationPartieService", informationPartieService);
    }

    @Test
    void calculMouvements() {

        String plateau="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Partie partie=notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBisService.calculMouvements(partie);
        
        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups=nbCoups(res);
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(20, nbCoups);
    }

    @Test
    void calculMouvements2() {

        String plateau="r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
        Partie partie=notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBisService.calculMouvements(partie);

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups=nbCoups(res);
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(48, nbCoups);
    }

    private static Stream<Arguments> provideCalculMouvementsParameters() {
        return Stream.of(
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 20),
                Arguments.of("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -", 48),
                Arguments.of("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ", 14),
                Arguments.of("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - - 0 1", 24)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCalculMouvementsParameters")
    void calculMouvementsParameters(String plateau, long nbCoupsRef) {

        Partie partie=notationFEN.createPlateau(plateau);

        // methode testée
        var res = calculMouvementBisService.calculMouvements(partie);

        // vérifications
        LOGGER.info("res={}", res);
        long nbCoups=nbCoups(res);
        LOGGER.info("nbCoups={}", nbCoups);
        assertEquals(nbCoupsRef, nbCoups);
    }


    private static Stream<Arguments> provideCalculPerf() {
        return Stream.of(
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",1, 20),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",2, 400),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",3, 8_902),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",4, 197_281),
                //Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",5, 4_865_609),
                //Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",6, 119_060_324),
                Arguments.of("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 1",1, 18),

                //Arguments.of("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -", 48),
                //Arguments.of("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ", 14),
                //Arguments.of("n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - - 0 1", 24)
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -",1, 5),
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -",1, 39),
                Arguments.of("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -",1, 237),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -",1, 17),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -",2, 341),
                Arguments.of("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -",3, 6666),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -",1, 9),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -",2, 85),
                Arguments.of("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -",3, 795),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -",1, 29),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -",2, 953),
                Arguments.of("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -",3, 27990)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCalculPerf")
    void calculPerf(String plateau, int depth, long perfRef) {

        Partie partie=notationFEN.createPlateau(plateau);

        // methode testée
        long res = calculPerf(partie,depth);

        // vérifications
        LOGGER.info("res={}", res);
        assertEquals(perfRef, res);
    }

    @Test
    void calculPerfTest() {

        String plateau="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        final int max=4;

        for(int i=0;i<=max;i++) {

            int depth=i;

            LOGGER.info("depth={}", depth);

            Partie partie = notationFEN.createPlateau(plateau);

            // methode testée
            Instant debut = Instant.now();
            long res = calculPerf(partie, depth);
            Instant fin = Instant.now();

            // vérifications

            LOGGER.info("res={}", res);
            LOGGER.info("duree={}", Duration.between(debut, fin));
            //assertEquals(perfRef, res);
        }
    }


    @Test
    void calculPerfTest2() {

        String plateau="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        final int max=4;

        for(int i=0;i<=max;i++) {

            int depth=i;

            LOGGER.info("depth={}", depth);

            Partie partie = notationFEN.createPlateau(plateau);

            PlateauBis plateau2=new PlateauBis(partie.getPlateau());

            // methode testée
            Instant debut = Instant.now();
            long res = calculPerf(plateau2, partie.getJoueurCourant(), depth);
            Instant fin = Instant.now();

            // vérifications

            LOGGER.info("res={}", res);
            LOGGER.info("duree={}", Duration.between(debut, fin));
            //assertEquals(perfRef, res);
        }
    }

    @Test
    void calculPerfTest3() {

        String plateau="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        final int max=4;

        //for(int i=0;i<=max;i++) {

            int depth=4;

            LOGGER.info("depth={}", depth);

            Partie partie = notationFEN.createPlateau(plateau);

            Plateau plateau2=partie.getPlateau();

            if(false) {
                plateau2 = new PlateauBis(partie.getPlateau());
            }

            // methode testée
            Instant debut = Instant.now();
            long res = calculPerf(plateau2, partie.getJoueurCourant(), depth);
            Instant fin = Instant.now();

            // vérifications

            LOGGER.info("res={}", res);
            LOGGER.info("duree={}", Duration.between(debut, fin));
            //assertEquals(perfRef, res);
        //}

        LongSummaryStatistics res2 = calculMouvementBisService.getDureeTotal().stream().mapToLong(x -> x.toMillis()).summaryStatistics();
        LOGGER.info("res2={}",res2);

        //LOGGER.info("stopWatch={}",calculMouvementBisService.getStopWatch().prettyPrint());
        LOGGER.info("stopWatch2={}",calculMouvementBisService.getStopWatch2());

        LOGGER.info("stopWatch liste depl={}",calculMouvementBisService.getStopWatchListeDeplacement());
        LOGGER.info("stopWatch genere mvt={}",calculMouvementBisService.getStopWatchGenereDeplacement());
        LOGGER.info("stopWatch suppr echecs={}",calculMouvementBisService.getStopWatchSupprEchecs());
    }

    // methodes utilitaires

    private long nbCoups(ListeMouvements2 listeMouvements){
        long nb=0;
        for(Entry<PieceCouleurPosition, List<IMouvement>> entry:listeMouvements.getMapMouvements().entrySet()){
            nb+=entry.getValue().size();
        }
        return nb;
    }

    private long calculPerf(Partie partie, int depth) {

        return calculPerf(partie.getPlateau(), partie.getJoueurCourant(),depth);
    }

    private long calculPerf(Plateau plateau, Couleur joueurCourant, int depth) {
        long resultat = 0;

        if(depth<=0){
            resultat= 1;
        } else {
            var res = calculMouvementBisService.calcul(plateau, joueurCourant);

            var map = res.getMapMouvements();
            assertNotNull(map);
            if (!map.isEmpty()) {
                var iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<PieceCouleurPosition, List<IMouvement>> tmp = iter.next();
                    if (!CollectionUtils.isEmpty(tmp.getValue())) {
                        for (var tmp2 : tmp.getValue()) {
//                            if(plateau instanceof PlateauBis){
//                                PlateauBis plateau2= (PlateauBis) plateau;
//                                plateau2.move(tmp.getKey().getPosition(), tmp2.getPosition());
//                                resultat += calculPerf(plateau2, calculMouvementBisService.joueurAdversaire(joueurCourant), depth - 1);
//                                plateau2.undo();
//                            } else {
                                //Partie partie2 = new Partie(partie);
                                Plateau plateau2 = new Plateau(plateau);
                                //assertEquals(joueurCourant, partie2.getJoueurCourant());
                                //plateau2.move(tmp.getKey().getPosition(), tmp2.getPosition());
                            plateau2.move(tmp.getKey().getPosition(), tmp2);
                                //partie2.setMove(tmp.getKey().getPosition(), tmp2.getPosition());
                                //assertEquals(calculMouvementBisService.joueurAdversaire(joueurCourant), partie2.getJoueurCourant());
                                resultat += calculPerf(plateau2, calculMouvementBisService.joueurAdversaire(joueurCourant), depth - 1);
//                            }
                        }
                    }
                }
            }
        }
        return resultat;
    }

}