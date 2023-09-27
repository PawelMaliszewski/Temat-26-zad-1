class Bet {
    constructor(betId, betMoney, rate, moneyToWin, notActive, betGames) {
        this.betId = betId;
        this.betMoney = betMoney;
        this.rate = rate;
        this.moneyToWin = moneyToWin;
        this.notActive = notActive;
        this.betGames = betGames;
    }
}

class BetGame {
    constructor(gameId, gameTitle, betFor, gameResult, winRate) {
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.betFor = betFor;
        this.gameResult = gameResult;
        this.winRate = winRate;
    }
}

let tempBetMoney = 0.0;
let betGamesMapList = new Map();
let bet;

let divBetList = document.getElementById("bet-game-list");
document.getElementById('betMoney').addEventListener('input', () => {
    tempBetMoney = document.getElementById('betMoney').value;
    appendOnChange()
    betGamesList();
})
showInputAndButton();

function setRadioButtons() {
    for (let [k, v] of betGamesMapList) {
        if (v.gameResult === 'TEAM_A_WON') {
            document.getElementById('ag' + k).setAttribute('checked', '')
        }
        if (v.gameResult === 'DRAW') {
            document.getElementById('bg' + k).setAttribute('checked', '')
        }
        if (v.gameResult === 'TEAM_B_WON') {
            document.getElementById('cg' + k).setAttribute('checked', '')
        }
    }
}

if ((localStorage.getItem('fromLocalStorageBetGamesList'))) {
    betGamesMapList = new Map(Object.entries(JSON.parse(localStorage.getItem('fromLocalStorageBetGamesList'))))
    tempBetMoney = localStorage.getItem('tempBetMoney')
    bet = new Bet(null, 0.00, 0.0, 0.0, 0, null);
    bet.betMoney = tempBetMoney;
    setRadioButtons()
    betGamesList()
} else {
    bet = new Bet(null, 0.00, 0.0, 0.0, 0, null);
}

function createTempBetGamesDiv() {
    let betGamesTemp = document.createElement('div');
    betGamesTemp.setAttribute('id', 'bet-game-child-temp')
    divBetList.appendChild(betGamesTemp);
    return betGamesTemp;
}

function betGamesList() {
    let rateSum = 0.00;
    let divInsideBetGameChild = document.getElementById('bet-game-child-temp')
    if (divInsideBetGameChild != null) {
        divInsideBetGameChild.remove();
    }
    if (betGamesMapList.size > 0) {
        let betGamesTemp = createTempBetGamesDiv();
        let ulList = document.createElement('ul');
        ulList.setAttribute('class', 'list-group-item');
        betGamesTemp.appendChild(ulList);
        for (let [k, v] of betGamesMapList) {
            rateSum += (parseFloat(v.winRate));
            const gameTitle = document.createElement("l1");
            gameTitle.setAttribute('class', 'list-group-item list-group-item-secondary');
            ulList.appendChild(gameTitle).innerHTML = "<b>Drużyny:</b> " + v.gameTitle;
            const winningConditionAndRate = document.createElement("li");
            winningConditionAndRate.setAttribute('class', 'list-group-item list-group-item-secondary');
            ulList.appendChild(winningConditionAndRate).innerHTML = "<b>Zakład na:</b> " + v.betFor + "<br><b>Kurs:</b> " + v.winRate;
        }
        let dataBet = new Bet(null, tempBetMoney, 0.0, 0.0, 0, null);
        dataBet.betGames = Array.from(betGamesMapList.values());
        let data = JSON.stringify(dataBet);
        $.ajax({
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            type: 'POST',
            url: 'bet_get_quote',
            data: data,
            success: function (data) {
                $(data).each(function (i, value) {
                    const moneyToWin = document.createElement("li");
                    moneyToWin.setAttribute('class', 'list-group-item list-group-item-warning')
                    moneyToWin.setAttribute('id', 'moneyToWin')
                    if (betGamesMapList.size.valueOf() > 1) {
                        ulList.appendChild(moneyToWin).innerHTML = "<b>Kurs całkowity:</b> " + value.rate + "<br>"
                            + ((tempBetMoney > 0) ? "<b>Do wygrania:</b> " + value.moneyToWin +"zł" : "");
                    } else if (tempBetMoney > 0) {
                        ulList.appendChild(moneyToWin).innerHTML = "<b>Do wygrania:</b> " + value.moneyToWin + "zł";
                    }
                    showInputAndButton();
                    appendOnChange();
                })
            },
            error: function () {
                alert("Błąd serwera nie udało się pobrać wartości zakładu")
            }
        })
    }
}

function showInputAndButton() {
    let betDiv = document.getElementById("bet-money-div");
    let button = document.getElementById("coupon-button");
    if (betGamesMapList.size.valueOf() === 0) {
        betDiv.style.display = 'none';
        button.style.display = 'none';
    } else {
        betDiv.style.display = "block";
        document.getElementById('betMoney').setAttribute('value', tempBetMoney)
    }
    if (tempBetMoney > 0) {
        button.style.display = 'block'
    }
}

function sendList() {
    bet.betMoney = tempBetMoney;
    bet.betGames = Array.from(betGamesMapList.values());
    const data = JSON.stringify(bet);
    $.ajax({
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        type: 'POST',
        url: 'bet',
        data: data,
        success: function (data) {
            location.replace('bet?betId=' + data.betId)
            localStorage.clear()
        },
        error: function () {
            alert("Niektóre mecze się zakończyły, zacznij on nowa.")
            localStorage.clear();
            location.reload();
        }
    })
    betGamesMapList = new Map()
    tempBetMoney = null;
    bet = null;
}

function addBetGameToList(jsData) {
    let jsDataArray = jsData.split(";");
    betGamesMapList.set(jsDataArray[0], new BetGame(jsDataArray[0], jsDataArray[1], jsDataArray[2],
        jsDataArray[3], jsDataArray[4]));
    betGamesList();
}

function removeGame(gameId) {
    betGamesMapList.delete(gameId);
    if (betGamesMapList.size === 0) {
        tempBetMoney = 0.0;
    }
    betGamesList()
}

function appendOnChange() {
    localStorage.fromLocalStorageBetGamesList = JSON.stringify(Object.fromEntries(betGamesMapList));
    localStorage.setItem('tempBetMoney', tempBetMoney)
}







