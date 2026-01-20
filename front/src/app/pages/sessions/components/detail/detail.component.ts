import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Teacher } from '../../../../core/models/teacher/teacher.interface';
import { SessionService } from '../../../../core/service/auth/session.service';
import { TeacherService } from '../../../../core/service/teacher/teacher.service';
import { Session } from '../../../../core/models/session/session.interface';
import { SessionApiService } from '../../../../core/service/session/session-api.service';
import { MaterialModule } from "../../../../shared/material.module";
import { CommonModule } from "@angular/common";
import { Observable, Subject, firstValueFrom } from 'rxjs';
import { startWith, switchMap, map } from 'rxjs/operators';

@Component({
  selector: 'app-detail',
  imports: [CommonModule, MaterialModule],
  standalone: true,
  styleUrls: ['./detail.component.scss'],
  template: `
    <div class="m3">
      @if (session$ | async; as session) {
        <mat-card>
          <mat-card-title>
            <div fxLayout="row" fxLayoutAlign="space-between center">
              <div fxLayout="row" fxLayoutAlign="start center">
                <button mat-icon-button (click)="back()">
                  <mat-icon>arrow_back</mat-icon>
                </button>
                <h1>{{session.name | titlecase}}</h1>
              </div>
              <div>
                @if (isAdmin$ | async) {
                  <button mat-raised-button color="warn" (click)="delete()">
                    <mat-icon>delete</mat-icon>
                    <span class="ml1">Delete</span>
                  </button>
                } @else {
                  @if (!(isParticipate$ | async)) {
                    <button mat-raised-button color="primary" (click)="participate()">
                      <mat-icon>person_add</mat-icon>
                      <span class="ml1">Participate</span>
                    </button>
                  } @else {
                    <button mat-raised-button color="warn" (click)="unParticipate()">
                      <mat-icon>person_remove</mat-icon>
                      <span class="ml1">Do not participate</span>
                    </button>
                  }
                }
              </div>
            </div>
          </mat-card-title>
        @if (teacher$ | async; as teacher) {
          <mat-card-subtitle>
            <div fxLayoutAlign="start center"  class="ml3">
              <mat-icon>
                people
              </mat-icon>
              <span class="ml1">{{ teacher.firstName }} {{ teacher.lastName | uppercase }}</span>
            </div>
          </mat-card-subtitle>
        }
        <div mat-card-image fxLayoutAlign="center center">
          <img class="picture" src="assets/sessions.png" alt="Yoga session">
        </div>
        <mat-card-content>
          <div class="my2" fxLayout="row" fxLayoutAlign="space-between center">
            <div fxLayout="row" fxLayoutAlign="start center">
              <mat-icon>
                group
              </mat-icon>
              <span class="ml1">{{ session.users.length }} attendees</span>
            </div>
            <div fxLayout="row" fxLayoutAlign="start center">
              <mat-icon>
                calendar_month
              </mat-icon>
              <span class="ml1">{{ session.date | date: 'longDate'}}</span>
            </div>
          </div>
          <div class="description">
            <p>Description:</p>
            {{ session.description }}
            <br>
          </div>
          <div class="date my2" fxLayout="row" fxLayoutAlign="space-between center">
            <div class="created"><i>Create at: </i> {{ session.createdAt| date: 'longDate'}}</div>
            <div class="updated"><i>Last update: </i> {{ session.updatedAt| date: 'longDate'}}</div>
          </div>
        </mat-card-content>
      </mat-card>
    }
    </div>
`,
})
export class DetailComponent implements OnInit {
  public session$!: Observable<Session>;
  public teacher$!: Observable<Teacher>;
  public isParticipate$!: Observable<boolean>;
  public isAdmin$!: Observable<boolean>;

  private refresh$ = new Subject<void>();
  public sessionId: string;
  public userId: string;

  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);
  private sessionService = inject(SessionService);
  private sessionApiService = inject(SessionApiService);
  private teacherService = inject(TeacherService);
  private matSnackBar = inject(MatSnackBar);
  private router = inject(Router);

  constructor() {
    this.sessionId = this.route.snapshot.paramMap.get('id')!;
    this.isAdmin$ = this.sessionService.isLogged$.pipe(
        map(() => !!this.sessionService.sessionInformation?.admin)
      );
    this.userId = this.sessionService.sessionInformation!.id.toString();
  }

  ngOnInit(): void {
    this.session$ = this.refresh$.pipe(
      startWith(void 0),
      switchMap(() => this.sessionApiService.detail(this.sessionId))
    );
    this.teacher$ = this.session$.pipe(
      switchMap((session: Session) => this.teacherService.detail(session.teacherId.toString()))
    );
    this.isParticipate$ = this.session$.pipe(
      map((session: Session) => session.users.some(u => u.id === this.sessionService.sessionInformation!.id))
    );
  }

  public back() {
    window.history.back();
  }

 public async delete(): Promise<void> {
    await firstValueFrom(this.sessionApiService.delete(this.sessionId));
    this.matSnackBar.open('Session deleted !', 'Close', { duration: 3000 });
    void this.router.navigate(['sessions']);
  }

  public async participate(): Promise<void> {
    await firstValueFrom(this.sessionApiService.participate(this.sessionId, this.userId));
    this.refresh$.next();
  }

  public async unParticipate(): Promise<void> {
    await firstValueFrom(this.sessionApiService.unParticipate(this.sessionId, this.userId));
    this.refresh$.next();
  }
}
