import { Component, OnInit } from '@angular/core';
import * as Chart from 'chart.js';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit
{
    ngOnInit(): void
    {
        let data = {
            '06:00': 512,
            '08:00': 600,
            '10:00': 457,
            '12:00': 453,
            '14:00': 484,
            '16:00': 470,
            '18:00': 500,
            '20:00': 512,
            '22:00': 589
        };

        new Chart('ram-chart', {
            type: 'line',
            data: {
                labels: Object.keys(data),
                datasets: [{
                    label: 'RAM (Mo)',
                    backgroundColor: '#97f722',
                    fill: true,
                    data: Object.values(data)
                }]
            },
            options: {
                title: {
                    display: true,
                    text: 'Consommation de la RAM (Mo)',
                    fontSize: 14
                },
                legend: {
                    display: false
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: 1200
                        }
                    }]
                },
                responsive: true
            }
        });
    }
}
