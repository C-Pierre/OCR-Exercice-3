import { Component } from '@angular/core';
import {Router, RouterModule, RouterOutlet} from '@angular/router';
import { Observable } from 'rxjs';
import { SessionService } from './core/service/auth/session.service';
import {CommonModule} from "@angular/common";
import {MaterialModule} from "./shared/material.module";
import { AuthService } from './core/service/auth/auth.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, MaterialModule, RouterOutlet, RouterModule],
  standalone: true,
  styleUrls: ['./app.component.scss'],
  template: `
    <div class="app">
      <mat-toolbar color="primary" fxLayout="row" fxLayoutAlign="space-between center">
        <span>Yoga app</span>
        @if (isLogged | async) {
          <div>
            <span routerLink="sessions" class="link">Sessions</span>
            <span routerLink="me" class="link">Account</span>
            <span (click)="logout()" class="link">Logout</span>
          </div>
        } @else {
          <div>
            <span routerLink="login" class="link">Login</span>
            <span routerLink="register" class="link">Register</span>
          </div>
        }
      </mat-toolbar>
      <div>
        <router-outlet></router-outlet>
      </div>
    </div>
  `
})
export class AppComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
    private sessionService: SessionService) {
  }

  public get isLogged(): Observable<boolean> {
    return this.sessionService.isLogged$;
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
  }
}
