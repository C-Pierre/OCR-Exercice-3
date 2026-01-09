import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../core/service/session.service';
import { SessionApiService } from '../../../../core/service/session-api.service';
import { TeacherService } from '../../../../core/service/teacher.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

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
    users: [1, 2, 3]
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

  const mockActivatedRoute = {
    snapshot: { paramMap: { get: () => '1' } },
    root: {} // 👈 nécessaire pour inject(ActivatedRoute)
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailComponent, RouterTestingModule],
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

  it('should correctly set isParticipate$', (done) => {
    component.isParticipate$.subscribe(isParticipating => {
      expect(isParticipating).toBe(true);
      done();
    });
  });

  it('should load session and teacher details on init', async () => {
    component.ngOnInit();
    component.session$.subscribe(session => {
      expect(session).toEqual(mockSession);
    });
    component.teacher$.subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    });
  });

  it('should call delete and navigate when delete is invoked', async () => {
    await component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call participate and refresh session on participate()', async () => {
    const nextSpy = jest.spyOn(component['refresh$'], 'next');
    await component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalled();
    expect(nextSpy).toHaveBeenCalled();
  });

  it('should call unParticipate and refresh session on unParticipate()', async () => {
    const nextSpy = jest.spyOn(component['refresh$'], 'next');
    await component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalled();
    expect(nextSpy).toHaveBeenCalled();
  });
});
