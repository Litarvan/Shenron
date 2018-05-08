import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

import { webcomponentsReady } from '@codebakery/origami';

if (environment.production) {
  enableProdMode();
}

webcomponentsReady().then(() => {
    platformBrowserDynamic().bootstrapModule(AppModule)
        .catch(err => console.log(err));
}).catch(error => {
  console.error(error);
});
