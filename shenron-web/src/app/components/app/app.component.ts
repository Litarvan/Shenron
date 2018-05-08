import { Component, OnInit } from '@angular/core';
import * as Chart from 'chart.js';
import { routerAnimation } from '../../animations/router.animation';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    animations: [routerAnimation]
})
export class AppComponent implements OnInit
{
    ngOnInit(): void
    {
        Chart.defaults.global.defaultFontFamily = "'Roboto'";
    }

    getRouteAnimation(outlet)
    {
        return outlet.activatedRouteData.animation
    }
}
