
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
}

class BetGame {
    constructor(gameId, gameTitle, betFor, gameResult, winRate) {
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.betFor = betFor;
        this.gameResult=gameResult;
        this.winRate = winRate;
    }
}

let tempBetMoney = 0.0;
let bet;
let betGamesMapList = new Map();
let divBetList = document.getElementById("bet-game-list");
document.getElementById('betMoney').addEventListener('input', () => {
    tempBetMoney = document.getElementById('betMoney').value;
    betGamesList();
})
showInputAndButton();

if ((localStorage.getItem('fromLocalStorageBetGamesList'))) {
    gameList.innerHTML = savedGameList;
    betGamesMapList = new Map(Object.entries(JSON.parse(localStorage.getItem('fromLocalStorageBetGamesList'))))
    console.log('in storage')
    tempBetMoney = localStorage.getItem('tempBetMoney')
    console.log("BetMoney from storage:" + tempBetMoney)
    betGamesList()
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

    bet = new Bet(null, tempBetMoney, rateSum, finalSum , 0, null);
    console.log("new Bet from betlist() : " + bet.betMoney)
    showInputAndButton();
    setRadioButtons();
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
        document.getElementById('betMoney').setAttribute('value',  tempBetMoney)
        console.log("else block :" + tempBetMoney)


    }
    if (tempBetMoney > 0) {button.style.display = 'block'}
}

function setRadioButtons() {
    for (let [k, v] of betGamesMapList) {
        switch(v.gameResult) {
            case 'TEAM_A_WON':
                console.log("Klucz ID dla radia: " + 'ag' + k)
                console.log(document.getElementById('ag'+k))
                document.getElementById('ag'+k).setAttribute('checked', '')
                break;
            case 'DRAW':
                document.getElementById('bg'+k).setAttribute('checked', '')
                console.log("Klucz ID dla radia: " + 'bg' + k)
                break;
            case 'TEAM_B_WON':
                document.getElementById('cg'+ k).setAttribute('checked', '')
                console.log("Klucz ID dla radia: " + 'bg' + k)
                break;
            default:
                alert("Nie udało sie odnaleźć ID dla radia !!!")
        }
}
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
            localStorage.clear()
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
    localStorage.fromLocalStorageBetGamesList = JSON.stringify(Object.fromEntries(betGamesMapList));
    localStorage.setItem('tempBetMoney', tempBetMoney)
}







