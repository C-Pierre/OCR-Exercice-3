import { Component } from '@angular/core';

@Component({
  selector: 'app-not-found',
  styleUrls: ['./not-found.component.scss'],
  standalone: true,
  template: `
    <div class="flex justify-center mt3">
            <h1>Page not found !</h1>
    </div>`,
})
export class NotFoundComponent {
}
