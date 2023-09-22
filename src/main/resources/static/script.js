class BetGame {
    constructor(gameId, gameTitle, betFor, winRate) {
        this._gameId = gameId;
        this._gameTitle = gameTitle;
        this._betFor = betFor;
        this._winRate = winRate;
    }

    get gameId() {
        return this._gameId;
    }

    set gameId(value) {
        this._gameId = value;
    }

    get gameTitle() {
        return this._gameTitle;
    }

    set gameTitle(value) {
        this._gameTitle = value;
    }

    get betFor() {
        return this._betFor;
    }

    set betFor(value) {
        this._betFor = value;
    }

    get winRate() {
        return this._winRate;
    }

    set winRate(value) {
        this._winRate = value;
    }
}

let betMoney;
const betGamesMapList = new Map();
let divBetList = document.getElementById("bet-game-list");
document.getElementById('betMoney').addEventListener('input', () => {
    betMoney = document.getElementById('betMoney').value;
    console.log(betMoney);
    betGamesList();
})
showInputAndButton();



function createTempBetGamesDiv() {
    let betGamesTemp = document.createElement('div');
    betGamesTemp.setAttribute('id', 'bet-game-child-temp')
    divBetList.appendChild(betGamesTemp);
    return betGamesTemp;
}

function betGamesList() {
    let rateSum = 0.00;
    let divInsideBetGameChild = document.getElementById('bet-game-child-temp')
    console.log(divInsideBetGameChild)
    if (divInsideBetGameChild != null) {
        divInsideBetGameChild.remove();
    }

    let betGamesTemp = createTempBetGamesDiv();

    let ulList = document.createElement('ul');
    ulList.setAttribute('class', 'list-group-item');
    betGamesTemp.appendChild(ulList)

    for (let [k, v] of betGamesMapList) {
        rateSum += (parseFloat(v._winRate));
        const gameTitle = document.createElement("l1");
        gameTitle.setAttribute('class', 'list-group-item list-group-item-secondary')
        ulList.appendChild(gameTitle).innerHTML = "<b>Drużyny:</b> " + v._gameTitle;
        const winningConditionAndRate = document.createElement("li");
        winningConditionAndRate.setAttribute('class', 'list-group-item list-group-item-secondary')
        ulList.appendChild(winningConditionAndRate).innerHTML = "<b>Zakład na:</b> " + v._betFor + "<br><b>Kurs:</b> " + v._winRate;
    }
    let finalSum = (betGamesMapList.size === 1) ? rateSum : rateSum * .84;
    rateSum = rateSum.toFixed(2)
    if (betMoney > 0) {
        finalSum = (betMoney * finalSum).toFixed(2);
    }
    const moneyToWin = document.createElement("li");
    moneyToWin.setAttribute('class', 'list-group-item list-group-item-warning')
    moneyToWin.setAttribute('id', 'moneyToWin')
    if (betGamesMapList.size.valueOf() > 1){
        ulList.appendChild(moneyToWin).innerHTML = "<b>Kurs całkowity:</b> " + rateSum + "<br>"
            + ((betMoney > 0) ? "<b>Do wygrania:</b> " + finalSum + "zł" : "");
    } else if (betMoney > 0){
        ulList.appendChild(moneyToWin).innerHTML = "<b>Do wygrania:</b> " + finalSum + "zł";
    }
    showInputAndButton();
}

function showInputAndButton() {
    let betDiv = document.getElementById("bet-money-div");
    let button = document.getElementById("coupon-button");
    console.log(betGamesMapList.size.valueOf() === 0);
    if (betGamesMapList.size.valueOf() === 0) {
        betDiv.style.display = 'none';
        button.style.display = 'none';
    } else {
        betDiv.style.display = "block";
    }
    if (betMoney > 0) {button.style.display = 'block'}

}

function sendList() {
    const data = JSON.stringify(Object.fromEntries(betGamesMapList));
    console.log(data);
    $.ajax({
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        type: 'POST',
        url: '/',
        data: data,
        success: listSent()
        })
}

function listSent() {
    alert("Udało się")
}

function addBetGameToList(jsData) {
    let jsDataArray = jsData.split(";");
    betGamesMapList.set(jsDataArray[0], new BetGame(jsDataArray[0], jsDataArray[1], jsDataArray[2],
        jsDataArray[3]));
    console.clear()
    betGamesList()
}

function removeGame(gameId) {
    betGamesMapList.delete(gameId);
    console.clear()
    betGamesList()
}

