import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../core/service/auth/session.service';
import { SessionApiService } from '../../../../core/service/session/session-api.service';
import { TeacherService } from '../../../../core/service/teacher/teacher.service';
import { Router, ActivatedRoute } from '@angular/router';
import { firstValueFrom, of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { Component } from '@angular/core';

@Component({ template: '<p>Dummy</p>' })
class DummyComponent {}

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSession = {
    id: 1,
    name: 'Yoga',
    description: 'Session description',
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
    teacherId: 10,
    users: []
  };

  const mockTeacher = {
    id: 10,
    firstName: 'Jane',
    lastName: 'Doe',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: {
      id: 1,
      admin: true,
      token: 'fake-token',
      type: 'user',
      username: 'jdoe',
      firstName: 'John',
      lastName: 'Doe'
    },
    isLogged$: of(true)
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of(null)),
    participate: jest.fn().mockReturnValue(of(null)),
    unParticipate: jest.fn().mockReturnValue(of(null))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockRouter = { navigate: jest.fn() };
  const mockActivatedRoute = { snapshot: { paramMap: { get: () => '1' } }, root: {} };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DetailComponent,
        RouterTestingModule.withRoutes([{ path: 'sessions', component: DummyComponent }])
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load session$ and teacher$', (done) => {
    let sessionLoaded = false;
    let teacherLoaded = false;

    component.session$.subscribe(session => {
      expect(session).toEqual(mockSession);
      expect(mockSessionApiService.detail).toHaveBeenCalledWith(component.sessionId);
      sessionLoaded = true;
      if (sessionLoaded && teacherLoaded) done();
    });

    component.teacher$.subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
      expect(mockTeacherService.detail).toHaveBeenCalledWith(mockSession.teacherId.toString());
      teacherLoaded = true;
      if (sessionLoaded && teacherLoaded) done();
    });
  });

  it('should call window.history.back() on back()', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(spy).toHaveBeenCalled();
    spy.mockRestore();
  });

  it('should delete session and navigate', async () => {
    await component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate and refresh session$', async () => {
    const nextSpy = jest.spyOn(component['refresh$'], 'next');
    await component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(nextSpy).toHaveBeenCalled();
  });

  it('should unParticipate and refresh session$', async () => {
    const nextSpy = jest.spyOn(component['refresh$'], 'next');
    await component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(nextSpy).toHaveBeenCalled();
  });

  describe('Integration tests (full flow)', () => {
    it('should correctly set isParticipate$ observable', async () => {
      const sessionWithUser = { ...mockSession, users: [{ id: 1 }] };
      (component['refresh$'] as any).next();
      (component['sessionApiService'].detail as jest.Mock).mockReturnValue(of(sessionWithUser));

      component.ngOnInit();

      const result = await firstValueFrom(component.isParticipate$);
      expect(result).toBe(true);
    });

    it('should complete delete flow and navigate', async () => {
      await component.delete();
      expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should complete participate flow', async () => {
      const nextSpy = jest.spyOn(component['refresh$'], 'next');
      await component.participate();
      expect(mockSessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
      expect(nextSpy).toHaveBeenCalled();
    });

    it('should complete unParticipate flow', async () => {
      const nextSpy = jest.spyOn(component['refresh$'], 'next');
      await component.unParticipate();
      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
      expect(nextSpy).toHaveBeenCalled();
    });

    it('should handle errors when loading session', async () => {
      (mockSessionApiService.detail as jest.Mock).mockReturnValueOnce(
        throwError(() => new Error('Session not found'))
      );
      component.ngOnInit();
      component.session$.subscribe({
        next: () => fail('should not succeed'),
        error: err => expect(err.message).toBe('Session not found')
      });
    });

    it('should handle errors when deleting session', async () => {
      (mockSessionApiService.delete as jest.Mock).mockReturnValueOnce(
        throwError(() => new Error('Delete failed'))
      );
      await component.delete().catch(err => {
        expect(err.message).toBe('Delete failed');
      });
    });
  });
});
