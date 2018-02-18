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
                    chess.plateau[i][j] = null;
                }
            }

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
                                    chess.plateau[i][j] = null;
                                }
                            }

                            for (var i = 0; i < donnees.listePieces.length; i++) {
                                var tmp = donnees.listePieces[i];

                                chess.plateau[tmp.ligne][tmp.colonne] = {
                                    piece: tmp.piece,
                                    couleur: (tmp.couleurBlanc==true) ? 'B' : 'N'
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
                        {params:{ name:'next'}})
                        .then(function (response) {
                            if (response.status == 200) {
                                console.error("next ok", response);
                            } else {
                                console.error("erreur pour le next : ", response);
                            }
                });

                //chess.initialise();
                console.log("next ok");
            };
        });