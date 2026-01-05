import { Component, OnInit, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from '../../core/models/user/user.interface';
import { SessionService } from '../../core/service/auth/session.service';
import { UserService } from '../../core/service/user/user.service';
import { MaterialModule } from "../../shared/material.module";
import { CommonModule } from "@angular/common";
import { firstValueFrom, Observable } from 'rxjs';

@Component({
  selector: 'app-me',
  imports: [CommonModule, MaterialModule],
  standalone: true,
  styleUrls: ['./me.component.scss'],
  template: `
    <div class="m3">
      <mat-card>
        <mat-card-title>
          <div fxLayout="row" fxLayoutAlign="start center">
            <button mat-icon-button (click)="back()">
              <mat-icon>arrow_back</mat-icon>
            </button>
            <h1>User information</h1>
          </div>
        </mat-card-title>
        <mat-card-content>
          @if (user$ | async; as user) {
            <div fxLayout="column" fxLayoutAlign="start center">
              <p>Name: {{ user.firstName }} {{ user.lastName | uppercase }}</p>
              <p>Email: {{ user.email }}</p>
              @if (user.admin) {
                <p class="my2">You are admin</p>
              } @else {
                <div fxLayout="column" class="my2" fxLayoutAlign="center center">
                  <p>Delete my account:</p>
                  <button mat-raised-button color="warn" (click)="delete()">
                    <mat-icon>delete</mat-icon>
                    <span class="ml1">Delete</span>
                  </button>
                </div>
              }
              <div fxLayoutAlign="space-between center" class="p2 w100">
                <p><i>Create at: </i> {{ user.createdAt | date: 'longDate'}}</p>
                <p><i>Last update: </i> {{ user.updatedAt| date: 'longDate'}}</p>
              </div>
            </div>
          }
        </mat-card-content>
      </mat-card>
    </div>
  `
})
export class MeComponent implements OnInit {
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private matSnackBar = inject(MatSnackBar);
  private userService = inject(UserService);
  public user$!: Observable<User>;


  ngOnInit(): void {
    this.user$ = this.userService.getById(this.sessionService.sessionInformation!.id.toString());
  }

  public back(): void {
    window.history.back();
  }

  public async delete(): Promise<void> {
    await
      firstValueFrom(this.userService.delete(this.sessionService.sessionInformation!.id.toString()));
      this.matSnackBar.open("Your account has been deleted !", 'Close', { duration: 3000 });
      this.sessionService.logOut();
      void this.router.navigate(['/']);
  }

}
