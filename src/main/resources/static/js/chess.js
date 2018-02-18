angular.module('chessApp', [])
        .controller('ChessController', function () {
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

            chess.plateau[0][0] = '*';
        });