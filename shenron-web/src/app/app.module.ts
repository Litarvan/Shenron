import { BrowserModule } from '@angular/platform-browser';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { PolymerModule } from '@codebakery/origami';

import { AppComponent } from './components/app/app.component';
import { MusicComponent } from './components/music/music.component';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ProfileComponent } from './components/profile/profile.component';
import { SettingsComponent } from './components/settings/settings.component';
import { GroupsComponent } from './components/groups/groups.component';

const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        data: { animation: 'home' }
    },
    {
        path: 'music',
        component: MusicComponent,
        data: { animation: 'music' }
    },
    {
        path: 'groups',
        component: GroupsComponent,
        data: { animation: 'groups' }
    },
    {
        path: 'profile',
        component: ProfileComponent,
        data: { animation: 'profile' }
    },
    {
        path: 'settings',
        component: SettingsComponent,
        data: { animation: 'settings' }
    }
];

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        MusicComponent,
        GroupsComponent,
        ProfileComponent,
        SettingsComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        PolymerModule.forRoot(),
        RouterModule.forRoot(routes)
    ],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [AppComponent],
    exports: [RouterModule]
})
export class AppModule
{
}
