import { Component, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/service/auth/auth.service';
import { RegisterRequest } from '../../core/models/auth/registerRequest.interface';
import { MaterialModule } from "../../shared/material.module";
import { CommonModule } from "@angular/common";
import { firstValueFrom } from 'rxjs';
@Component({
  selector: 'app-register',
  imports: [CommonModule, MaterialModule],
  standalone: true,
  styleUrls: ['./register.component.scss'],
  template: `
    <div class="register" fxLayout="row" fxLayoutAlign="center center">
      <mat-card>
          <mat-card-header fxLayoutAlign="center center">
              <mat-card-title>Register</mat-card-title>
          </mat-card-header>
          <form class="register-form" [formGroup]="form" (ngSubmit)="submit()">
              <mat-card-content fxLayout="column">
                  <mat-form-field fxFlex>
                      <input matInput placeholder="First name" formControlName="firstName">
                  </mat-form-field>
                  <mat-form-field fxFlex>
                      <input matInput placeholder="Last name" formControlName="lastName">
                  </mat-form-field>
                  <mat-form-field fxFlex>
                      <input matInput placeholder="Email" formControlName="email">
                  </mat-form-field>
                  <mat-form-field fxFlex>
                      <input matInput placeholder="Password" formControlName="password">
                  </mat-form-field>
              </mat-card-content>
              <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Submit</button>
              @if (onError) {
                <span class="error ml2">An error occurred</span>
              }
          </form>
      </mat-card>
    </div>
  `
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);
  public onError = false;

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    lastName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(40)]]
  });

    public async submit(): Promise<void> {
      if (this.form.invalid) {
        this.form.markAllAsTouched();
        return;
      }

      const registerRequest = this.form.value as RegisterRequest;
      try {
        await firstValueFrom(this.authService.register(registerRequest));
        this.onError = false;
        await this.router.navigate(['/login']);
      } catch (error) {
        this.onError = true;
      }
  }

}
