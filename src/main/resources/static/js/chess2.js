"use strict";

var board;

function loadPieces() {
    $.ajax({
        type: "GET",
        dataType: 'json',
        url: "http://localhost:8080/plateauFen",
        success: function (data) {
            console.log("loadPieces data :", data);
            //alert(data);
            //board.position('r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R');
            board.position(data.fen);
            loadEtat();
        },
        error: function (resultat, statut, erreur) {
            console.error("loadPieces : error", resultat, statut, erreur);

        }
    });
}


function loadEtat() {
    $.ajax({
        type: "GET",
        dataType: 'json',
        url: "http://localhost:8080/status",
        success: function (data) {
            console.log("loadEtat data :", data);
            $('#joueur').html(data.joueur);
            $('#etatJeux').html(data.etat);
        },
        error: function (resultat, statut, erreur) {
            console.error("loadEtat : error", resultat, statut, erreur);

        }
    });
}

function nextMove() {

    $.ajax({
        type: "GET",
        //dataType: 'json',
        url: "http://localhost:8080/action?nom=next",
        success: function (data) {
            console.log("nextMove data:", data);
            loadPieces();
        },
        error: function (resultat, statut, erreur) {
            console.error("nextMove : error", resultat, statut, erreur);

        }
    });
}

function loadListeJoueurs() {
    $.ajax({
        type: "GET",
        dataType: 'json',
        url: "http://localhost:8080/listeTypeJoueur",
        success: function (data) {
            console.log("loadListeJoueurs : data", data);

            var txt = "";
            for (var i = 0; i < data.length; i++) {
                var selected = (data[i] == "JOUEUR_HAZARD");
                txt += "<option value='" + data[i] + "' " + ((selected) ? "selected" : "") + ">" + data[i] + "</option>";
            }
            $('#joueurBlanc').html(txt);
            $('#joueurNoir').html(txt);

        },
        error: function (resultat, statut, erreur) {
            console.error("loadListeJoueurs : error", resultat, statut, erreur);

        }
    });
}


function demarrage() {

    var blanc = $("#joueurBlanc").val();
    var noir = $("#joueurNoir").val();

    var valeursInitiales = $("#valeursInitiales").val();

    console.info("blanc:", blanc, "noir:", noir, "valeurInitiales:", valeursInitiales);

    $.ajax({
        type: "GET",
        //dataType: 'json',
        url: "http://localhost:8080/demarrage?" +
        "joueurBlanc=" + blanc + "&joueurNoir=" + noir +
        "&valeursInitiales=" + valeursInitiales,
        success: function (data) {
            console.log("demarrage : data", data);

            loadPieces();
        },
        error: function (resultat, statut, erreur) {
            console.error("demarrage : error", resultat, statut, erreur);

        }
    });

}

function logInfos() {
    $.ajax({
        type: "GET",
        //dataType: 'json',
        url: "http://localhost:8080/logInfos",
        success: function (data) {
            console.log("logInfos : data", data);

        },
        error: function (resultat, statut, erreur) {
            console.error("logInfos : error", resultat, statut, erreur);

        }
    });
}

var removeGreySquares = function () {
    $('#myBoard .square-55d63').css('background', '');
};

var greySquare = function (square) {
    var squareEl = $('#myBoard .square-' + square);

    var background = '#a9a9a9';
    if (squareEl.hasClass('black-3c85d') === true) {
        background = '#696969';
    }

    squareEl.css('background', background);
};

var onDragStart = function (source, piece) {
    // // do not pick up pieces if the game is over
    // // or if it's not that side's turn
    // if (game.game_over() === true ||
    //         (game.turn() === 'w' && piece.search(/^b/) !== -1) ||
    //         (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
    //     return false;
    // }
};

var onDrop = function (source, target) {
    // removeGreySquares();
    //
    // // see if the move is legal
    // var move = game.move({
    //     from: source,
    //     to: target,
    //     promotion: 'q' // NOTE: always promote to a queen for example simplicity
    // });
    //
    // // illegal move
    // if (move === null) return 'snapback';
};

var getMoves = function (ligne, colonne, success) {
    console.log("getMoves", ligne, colonne);
    $.ajax({
        type: "GET",
        //dataType: 'json',
        url: "http://localhost:8080/deplacements/" + ligne + "/" + colonne,
        success: function (data) {
            console.log("deplacements : data", data);

            //loadPieces();
            if (success) {
                success(data);
            }
        },
        error: function (resultat, statut, erreur) {
            console.error("deplacements : error", resultat, statut, erreur);

        }
    });
};

// var getMoves2 = function (position) {
//     getMoves()
// };

var onMouseoverSquare = function (square, piece) {
    console.log("onMouseoverSquare", square, piece);
    // get list of possible moves for this square
    // var moves = game.moves({
    //     square: square,
    //     verbose: true
    // });
    var moves = [''];

    if (!!square && square.length == 2) {

        console.log("charAt", square.charAt(0), square.charAt(1));

        var colonne = square.charCodeAt(0) - 97;
        var ligne = square.charCodeAt(1) - 49;
        getMoves(ligne, colonne, function (data) {

            if (data.length == 0) return;

            // exit if there are no moves available for this square
            //if (moves.length === 0) return;

            // highlight the square they moused over
            greySquare(square);

            // highlight the possible squares for this piece
            for (var i = 0; i < data.length; i++) {
                var ligne2 = data[i].ligne;
                var colonne2 = data[i].colonne;
                var pos = String.fromCharCode(colonne2 + 97) + String.fromCharCode(ligne2 + 49);
                greySquare(pos);
            }
        });

        // // exit if there are no moves available for this square
        // if (moves.length === 0) return;
        //
        // // highlight the square they moused over
        // greySquare(square);

        // highlight the possible squares for this piece
        // for (var i = 0; i < moves.length; i++) {
        //     greySquare(moves[i].to);
        // }
    }

};

var onMouseoutSquare = function (square, piece) {
    removeGreySquares();
};

var onSnapEnd = function () {
    //board.position(game.fen());
};

function init() {
    //alert('reload cart called');

    var cfg = {
        //draggable: true,
        //position: 'start',
        //onDragStart: onDragStart,
        //onDrop: onDrop,
        onMouseoutSquare: onMouseoutSquare,
        onMouseoverSquare: onMouseoverSquare//,
        //onSnapEnd: onSnapEnd
    };

    // board = Chessboard('#myBoard', 'start');
    board = Chessboard('#myBoard', cfg);

    // $('#startPositionBtn').on('click', function () {
    //     //board.position('r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R');
    //     loadPieces();
    // });

    $('#nextBtn').on('click', function () {
        nextMove();
    });

    $('#StartBtn').on('click', function () {
        demarrage();
    });

    $('#logBtn').on('click', function () {
        logInfos();
    });

    loadListeJoueurs();

}





