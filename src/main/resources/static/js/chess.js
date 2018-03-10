angular.module('chessApp', [])
        .controller('ChessController', function ($scope, $http) {
            var chess = this;

            TAILLE = 8;

            chess.plateau = new Array(TAILLE);

            for (var i = 0; i < TAILLE; i++) {
                chess.plateau[i] = new Array(TAILLE);
            }

            for (var i = 0; i < TAILLE; i++) {
                for (var j = 0; j < TAILLE; j++) {
                    chess.plateau[i][j] = {
                        piece: null,
                        couleur: null,
                        ligne: i,
                        colonne: j
                    };
                }
            }

            chess.selection = null;
            chess.deplacement = null;

            chess.plateau[0][0] = {
                piece: 'P',
                couleur: 'B'
            };


            chess.plateau[0][1] = {
                piece: 'R',
                couleur: 'N'
            };


            chess.initialise = function () {

                $http.get('/plateau').then(function (response) {
                    if (response.status == 200) {
                        var donnees = response.data;

                        if (donnees && donnees.listePieces) {

                            for (var i = 0; i < TAILLE; i++) {
                                for (var j = 0; j < TAILLE; j++) {
                                    chess.plateau[i][j] = {
                                        piece: null,
                                        couleur: null,
                                        ligne: i,
                                        colonne: j
                                    };
                                }
                            }

                            for (var j = 0; j < donnees.listePieces.length; j++) {
                                var tmp = donnees.listePieces[j];

                                chess.plateau[tmp.ligne][tmp.colonne] = {
                                    piece: tmp.piece,
                                    couleur: (tmp.couleurBlanc == true) ? 'B' : 'N',
                                    ligne: tmp.ligne,
                                    colonne: tmp.colonne
                                }

                            }
                        }
                    } else {
                        console.error("erreur pour récupérer le plateau : ", response);
                    }
                });

            };

            chess.test1 = function () {
                console.log("test1");
                chess.initialise();
                console.log("suite");
            };

            chess.next = function () {
                console.log("next ...");


                $http.get('/action',
                        {params: {nom: 'next'}})
                        .then(function (response) {
                            if (response.status == 200) {
                                console.error("next ok", response);
                            } else {
                                console.error("erreur pour le next : ", response);
                            }
                            chess.initialise();
                        });

                //chess.initialise();
                console.log("next ok");
            };

            chess.clickCase = function (ligne, colonne) {
                console.log("click", ligne, colonne);

                if (ligne != null) {
                    $http.get('/deplacements/' + ligne.ligne + "/" + ligne.colonne)
                            .then(function (response) {
                                if (response.status == 200) {
                                    console.info("deplacements ok", response);
                                    chess.selection = null;
                                    chess.deplacement = null;
                                    if (response.data) {
                                        console.info("deplacements possible pour ", ligne.ligne, ligne.colonne);
                                        for (i = 0; i < response.data.length; i++) {
                                            pos = response.data[i];
                                            console.info("pos:", pos.ligne, pos.colonne);
                                        }
                                        chess.selection = {
                                            ligne: ligne.ligne,
                                            colonne: ligne.colonne
                                        };
                                        chess.deplacement = response.data;
                                    }
                                } else {
                                    console.error("erreur pour le deplacements : ", response);
                                }
                                //chess.initialise();
                            });
                }
                console.log("fin click");
            };

            chess.couleurCase = function (pos) {
                if (pos != null) {

                    var ligneSelectione = pos.ligne;
                    var colonneSelectione = pos.colonne;

                    //console.log("couleurCase", ligneSelectione, colonneSelectione);

                    if (chess.selection != null) {
                        if (ligneSelectione == chess.selection.ligne && colonneSelectione == chess.selection.colonne) {
                            return "couleur-case-selectione";
                        }
                    }

                    if (chess.deplacement != null) {

                        for (var i = 0; i < chess.deplacement.length; i++) {
                            var pos2 = chess.deplacement[i];

                            if (ligneSelectione == pos2.ligne && colonneSelectione == pos2.colonne) {
                                return "couleur-case-deplacement";
                            }
                        }
                    }

                }

                return "couleur-case-defaut";
            }

        });