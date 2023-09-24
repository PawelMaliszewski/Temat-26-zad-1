
let gameList = document.querySelector('#currentGamesList');
const savedGameList = localStorage.getItem('gameList');

class Bet {

    constructor(betId, betMoney, rate, moneyToWin, notActive, betGames) {
        this.betId = betId;
        this.betMoney = betMoney;
        this.rate = rate;
        this.moneyToWin = moneyToWin;
        this.notActive = notActive;
        this.betGames = betGames;
    }

    get bet_Money() {
        return this.betMoney;
    }

    set bet_Money(value) {
        this.betMoney = value;
    }

    get _rate() {
        return this.rate;
    }

    set _rate(value) {
        this.rate = value;
    }

    get money_To_Win() {
        return this.moneyToWin;
    }

    set money_To_Win(value) {
        this.moneyToWin = value;
    }

    get bet_Games() {
        return this.betGames;
    }

    set bet_Games(value) {
        this.betGames = value;
    }

}

class BetGame {
    constructor(gameId, gameTitle, betFor, gameResult, winRate) {
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.betFor = betFor;
        this.gameResult=gameResult;
        this.winRate = winRate;
    }

    get game_Title() {
        return this.gameTitle;
    }

    set game_Title(value) {
        this.gameTitle = value;
    }

    get bet_For() {
        return this.betFor;
    }

    set bet_For(value) {
        this.betFor = value;
    }

    get win_Rate() {
        return this.winRate;
    }

    set win_Rate(value) {
        this.winRate = value;
    }
}

let tempBetMoney;
let bet = new Bet();
let betGamesMapList = new Map();
let divBetList = document.getElementById("bet-game-list");
document.getElementById('betMoney').addEventListener('input', () => {
    tempBetMoney = document.getElementById('betMoney').value;
    console.log()
    betGamesList();
})
showInputAndButton();

if (savedGameList) {
    gameList.innerHTML = savedGameList;
    betGamesMapList = new Map(Object.entries(JSON.parse(localStorage.getItem('betGames'))))
    bet = new Bet(Object.entries(JSON.parse(localStorage.getItem('bet'))));
    betGamesList();
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
    let betGamesTemp = createTempBetGamesDiv();
    let ulList = document.createElement('ul');
    ulList.setAttribute('class', 'list-group-item');
    betGamesTemp.appendChild(ulList)
    for (let [k, v] of betGamesMapList) {
        rateSum += (parseFloat(v.winRate));
        const gameTitle = document.createElement("l1");
        gameTitle.setAttribute('class', 'list-group-item list-group-item-secondary')
        ulList.appendChild(gameTitle).innerHTML = "<b>Drużyny:</b> " + v.gameTitle;
        const winningConditionAndRate = document.createElement("li");
        winningConditionAndRate.setAttribute('class', 'list-group-item list-group-item-secondary')
        ulList.appendChild(winningConditionAndRate).innerHTML = "<b>Zakład na:</b> " + v.betFor  + "<br><b>Kurs:</b> " + v.winRate;
    }
    let finalSum = (betGamesMapList.size === 1) ? rateSum : rateSum * .84;
    rateSum = rateSum.toFixed(2)
    if (tempBetMoney > 0) {
        finalSum = (tempBetMoney * finalSum).toFixed(2);
    }
    const moneyToWin = document.createElement("li");
    moneyToWin.setAttribute('class', 'list-group-item list-group-item-warning')
    moneyToWin.setAttribute('id', 'moneyToWin')
    if (betGamesMapList.size.valueOf() > 1){
        ulList.appendChild(moneyToWin).innerHTML = "<b>Kurs całkowity:</b> " + rateSum + "<br>"
            + ((tempBetMoney > 0) ? "<b>Do wygrania:</b> " + finalSum + "zł" : "");
    } else if (tempBetMoney > 0){
        ulList.appendChild(moneyToWin).innerHTML = "<b>Do wygrania:</b> " + finalSum + "zł";
    }
    bet.rate = rateSum;
    bet.moneyToWin = finalSum.valueOf();
    bet.betMoney = tempBetMoney;
    showInputAndButton();
    appendOnChange();
}

function showInputAndButton() {
    let betDiv = document.getElementById("bet-money-div");
    let button = document.getElementById("coupon-button");
    if (betGamesMapList.size.valueOf() === 0) {
        betDiv.style.display = 'none';
        button.style.display = 'none';
    } else {
        betDiv.style.display = "block";

    }
    if (tempBetMoney > 0) {button.style.display = 'block'}
}

function sendList() {
    bet.betGames = Array.from(betGamesMapList.values());
    const data = JSON.stringify(bet);
    $.ajax({
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        type: 'POST',
        url: '/bet',
        data: data,
        success: function (data) {
            location.replace('/bet?betId='+ data.betId)
        },
        error: function (data) {
            alert("Niektóre mecze się zakończyły, zacznij on nowa.")
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
    betGamesList()
    ;
}

function removeGame(gameId) {
    betGamesMapList.delete(gameId);
    betGamesList()
}

function appendOnChange() {
    localStorage.setItem('gameList', gameList.innerHTML)
    localStorage.betGames = JSON.stringify(Object.fromEntries(betGamesMapList));
    localStorage.bet = JSON.stringify(bet);
}







