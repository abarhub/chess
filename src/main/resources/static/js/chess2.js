"use strict";

var board;

function loadPieces() {


    $.ajax({
        type: "GET",
        dataType: 'json',
        url: "http://localhost:8080/plateauFen",
        success: function (data) {
            console.log("data", data);
            //alert(data);
            //board.position('r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R');
            board.position(data.fen);
        }
    });
}

function nextMove() {

    $.ajax({
        type: "GET",
        //dataType: "jsonp",
        url: "http://localhost:8080/action?nom=next",
        success: function (data) {
            console.log("data", data);
            loadPieces();
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

        }
    });
}


function demarrage() {

    var blanc = $("#joueurBlanc").val();
    var noir = $("#joueurNoir").val();

    console.info("blanc:", blanc, "noir:", noir);

    $.ajax({
        type: "GET",
        //dataType: "jsonp",
        url: "http://localhost:8080/demarrage?" +
        "joueurBlanc=" + blanc + "&joueurNoir=" + noir,
        success: function (data) {
            console.log("demarrage : data", data);

            loadPieces();
        }
    });

}

function init() {
    //alert('reload cart called');

    // board = Chessboard('#myBoard', 'start');
    board = Chessboard('#myBoard');

    $('#startPositionBtn').on('click', function () {
        //board.position('r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R');
        loadPieces();
    });

    $('#nextBtn').on('click', function () {
        nextMove();
    });

    $('#StartBtn').on('click', function () {
        demarrage();
    });

    loadListeJoueurs();

}





