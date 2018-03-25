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

function init() {
    //alert('reload cart called');

    // board = Chessboard('#myBoard', 'start');
    board = Chessboard('#myBoard');

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





