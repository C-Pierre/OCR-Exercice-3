import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/core/models/auth/sessionInformation.interface';
import { SessionService } from 'src/app/core/service/auth/session.service';
import { LoginRequest } from '../../core/models/auth/loginRequest.interface';
import { AuthService } from '../../core/service/auth/auth.service';
import {MaterialModule} from "../../shared/material.module";
import { CommonModule } from '@angular/common';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [CommonModule, MaterialModule],
  standalone: true,
  styleUrls: ['./login.component.scss'],
  template: `
    <div class="login" fxLayout="row" fxLayoutAlign="center center">
      <mat-card>
          <mat-card-header fxLayoutAlign="center center">
              <mat-card-title>Login</mat-card-title>
          </mat-card-header>
          <form class="login-form" [formGroup]="form" (ngSubmit)="submit()">
              <mat-card-content fxLayout="column">
                  <mat-form-field fxFlex>
                    <input matInput placeholder="Email" formControlName="email">
                  </mat-form-field>
                  <mat-form-field fxFlex>
                    <input matInput [type]="hide ? 'password' : 'text'" placeholder="Password" formControlName="password">
                    <button mat-icon-button matSuffix (click)="hide = !hide" [attr.aria-label]="'Hide password'"
                        [attr.aria-pressed]="hide">
                        <mat-icon>{{hide ? 'visibility_off' : 'visibility'}}</mat-icon>
                    </button>
                  </mat-form-field>
              </mat-card-content>
              <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Submit</button>
              @if (onError) {
                <p class="error">An error occurred</p>
              }
          </form>
      </mat-card>
    </div>
  `
})
export class LoginComponent {
  public hide = true;
  public onError = false;

  public form = this.fb.group({
    email: [
      '',
      [
        Validators.required,
        Validators.email
      ]
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.min(3)
      ]
    ]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService) {
  }
    
  public async submit(): Promise<void> {
    const loginRequest = this.form.value as LoginRequest;
    try {
      const response: SessionInformation = await firstValueFrom(this.authService.login(loginRequest));
      this.sessionService.logIn(response);
      void this.router.navigate(['/sessions']);
    } catch (error) {
      this.onError = true;
    }
  }
}
