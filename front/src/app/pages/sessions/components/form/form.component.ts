import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SessionService } from '../../../../core/service/auth/session.service';
import { TeacherService } from '../../../../core/service/teacher/teacher.service';
import { Session } from '../../../../core/models/session/session.interface';
import { SessionApiService } from '../../../../core/service/session/session-api.service';
import { MaterialModule } from "../../../../shared/material.module";
import { CommonModule } from "@angular/common";
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-form',
  imports: [CommonModule, MaterialModule, RouterModule],
  standalone: true,
  styleUrls: ['./form.component.scss'],
  template: `
    <div class="create m3">
      <mat-card>
        <mat-card-title>
          <div fxLayout="row" fxLayoutAlign="start center">
            <button mat-icon-button routerLink="/sessions">
              <mat-icon>arrow_back</mat-icon>
            </button>
            @if (!onUpdate) {
              <h1>Create session</h1>
            } @else {
              <h1>Update session</h1>
            }
          </div>
        </mat-card-title>
          @if (sessionForm) {
            <mat-card-content>
            <form class="mt2" fxLayout="column" [formGroup]="sessionForm" (ngSubmit)="submit()">
            <mat-form-field appearance="outline" fxFlex>
              <mat-label>Name</mat-label>
              <input matInput formControlName="name">
            </mat-form-field>
            <mat-form-field appearance="outline" fxFlex>
              <mat-label>Date</mat-label>
              <input matInput type="date" formControlName="date">
            </mat-form-field>
            <mat-form-field appearance="outline" fxFlex>
              <mat-label>Teacher</mat-label>
              <mat-select ngDefaultControl formControlName="teacherId">
                @for (teacher of teachers$ | async; track teacher.id) {
                  <mat-option [value]="teacher.id">
                    {{teacher.firstName}} {{teacher.lastName}}
                  </mat-option>
                }
              </mat-select>
            </mat-form-field>
            <mat-form-field appearance="outline" fxFlex>
              <mat-label>Description</mat-label>
              <textarea matInput rows="8" formControlName="description"></textarea>
            </mat-form-field>
            <div fxLayout="row" fxLayoutAlign="center center">
              <button mat-raised-button color="primary" type="submit" [disabled]="sessionForm.invalid">
                Save
              </button>
            </div>
          </form>
          </mat-card-content>
        }
      </mat-card>
    </div>
  `,
})
export class FormComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);
  private matSnackBar = inject(MatSnackBar);
  private sessionApiService = inject(SessionApiService);
  private sessionService = inject(SessionService);
  private teacherService = inject(TeacherService);
  private router = inject(Router);

  public onUpdate: boolean = false;
  public sessionForm: FormGroup | undefined;
  public teachers$ = this.teacherService.all();
  private id: string | undefined;

  async ngOnInit(): Promise<void> {
    if (!this.sessionService.sessionInformation!.admin) {
      this.router.navigate(['/sessions']);
      return;
    }

    const url = this.router.url;
    if (url.includes('update')) {
      this.onUpdate = true;
      this.id = this.route.snapshot.paramMap.get('id')!;
      const session = await firstValueFrom(
        this.sessionApiService.detail(this.id)
      );
      this.initForm(session);
    } else {
      this.initForm();
    }
  }

  public async submit(): Promise<void> {
    if (!this.sessionForm || this.sessionForm.invalid) {
      return;
    }

    const session = this.sessionForm.value as Session;

    try {
      if (!this.onUpdate) {
        await firstValueFrom(this.sessionApiService.create(session));
        this.exitPage('Session created !');
      } else {
        await firstValueFrom(this.sessionApiService.update(this.id!, session));
        this.exitPage('Session updated !');
      }
    } catch (err) {
      return
    }
  }

  private initForm(session?: Session): void {
    this.sessionForm = this.fb.group({
      name: [session?.name || '', [Validators.required]],
      date: [session ? new Date(session.date).toISOString().split('T')[0] : '', [Validators.required]],
      teacherId: [session?.teacherId || '', [Validators.required]],
      description: [session?.description || '', [Validators.required, Validators.max(2000)]],
    });
  }

  private exitPage(message: string): void {
    this.matSnackBar.open(message, 'Close', { duration: 3000 });
    this.router.navigate(['sessions']);
  }
}
