const $searchBtn = document.querySelector('#search-date');
const sales_select = document.querySelector('#sales_select');

const labels = [];
const salesData = [];

// 날짜
let today = new Date();

let startDate = today.toISOString().substring(0,10);
let endDate = today.toISOString().substring(0,10);
const ctx = document.getElementById('myChart').getContext('2d');
var myChart;

myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: labels,
        datasets: [{
            label: '날짜별 판매금액',
            data: salesData,
            backgroundColor: [
                '#2b68ff'
            ]
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

function setChart(){
    myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: '날짜별 판매금액',
                data: salesData,
                backgroundColor: [
                    '#2b68ff'
                ]
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function destoryChart(){
    myChart.destroy();
}



$searchBtn.addEventListener('click', () => {
    getSales(startDate, endDate, sales_select.value);
})

async function getSales(startDate, endDate, sort){
    destoryChart();
    labels.length = 0;
    salesData.length = 0;

    console.log(startDate,endDate,sort)
    const res = await fetch(`/api/manager/sales/statistics/revenue?startTime=${startDate}&endTime=${endDate}&stateByPeriod=${sort}`);
    const data = await res.json();

    console.log(data);

    for(const item of data){
        labels.push(item.date);
        salesData.push(item.price);
    }
    setChart();
}

getSales(startDate, endDate, sales_select.value);
