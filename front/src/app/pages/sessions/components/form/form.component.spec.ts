import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormComponent } from './form.component';
import { SessionService } from '../../../../core/service/auth/session.service';
import { SessionApiService } from '../../../../core/service/session/session-api.service';
import { TeacherService } from '../../../../core/service/teacher/teacher.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = { sessionInformation: { admin: true } };
  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of(null)),
    update: jest.fn().mockReturnValue(of(null)),
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Yoga',
      date: new Date(),
      teacherId: 10,
      description: 'Session desc',
      createdAt: new Date(),
      updatedAt: new Date(),
      users: []
    }))
  };
  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([
      { id: 10, firstName: 'Jane', lastName: 'Doe', createdAt: new Date(), updatedAt: new Date() }
    ]))
  };
  const mockRouter = {
    navigate: jest.fn(),
    get url() { return '/sessions/create'; }
  };
  const mockActivatedRoute = { snapshot: { paramMap: { get: () => null } } };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormComponent, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values for creation', () => {
    expect(component.sessionForm).toBeDefined();
    expect(component.onUpdate).toBe(false);
    const formValue = component.sessionForm?.value;
    expect(formValue.name).toBe('');
    expect(formValue.description).toBe('');
    expect(formValue.teacherId).toBe('');
  });

  it('should call create on submit when onUpdate is false', async () => {
    component.sessionForm?.setValue({
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 10,
      description: 'Session desc'
    });

    await component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith({
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 10,
      description: 'Session desc'
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call update on submit when onUpdate is true', async () => {
    component.onUpdate = true;
    component['id'] = '1';
    component.sessionForm?.setValue({
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 10,
      description: 'Updated desc'
    });

    await component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', {
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 10,
      description: 'Updated desc'
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should not call create/update if form is invalid', async () => {
    component.sessionForm?.setValue({
      name: '',
      date: '',
      teacherId: '',
      description: ''
    });

    component.sessionForm?.markAllAsTouched();
    component.sessionForm?.updateValueAndValidity();

    await component.submit();

    expect(mockSessionApiService.create).not.toHaveBeenCalled();
    expect(mockSessionApiService.update).not.toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should handle create error gracefully', async () => {
    component.sessionForm?.setValue({
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 10,
      description: 'Session desc'
    });

    mockSessionApiService.create.mockReturnValueOnce(
      throwError(() => new Error('Create failed'))
    );

    await component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled();
  });

  it('should handle update error gracefully', async () => {
    component.onUpdate = true;
    component['id'] = '1';
    component.sessionForm?.setValue({
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 10,
      description: 'Session desc'
    });

    mockSessionApiService.update.mockReturnValueOnce(
      throwError(() => new Error('Update failed'))
    );

    await component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', {
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 10,
      description: 'Session desc'
    });
  });

  it('should initialize form for update when url contains "update"', async () => {
    const updateId = '123';
    const mockDetail = {
      id: 1,
      name: 'Yoga',
      date: new Date(),
      teacherId: 10,
      description: 'Session desc',
      createdAt: new Date(),
      updatedAt: new Date(),
      users: []
    };

    component['route'] = { snapshot: { paramMap: { get: () => updateId } } } as any;
    component['router'] = { navigate: jest.fn(), get url() { return `/sessions/update/${updateId}`; } } as any;
    component['sessionApiService'] = { detail: jest.fn().mockReturnValue(of(mockDetail)) } as any;

    const initSpy = jest.spyOn(component as any, 'initForm');

    await component.ngOnInit();

    expect(component.onUpdate).toBe(true);
    expect(component['id']).toBe(updateId);
    expect(component['sessionApiService'].detail).toHaveBeenCalledWith(updateId);
    expect(initSpy).toHaveBeenCalledWith(mockDetail);
  });
});


describe('FormComponent non-admin redirect', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  const mockRouter = { navigate: jest.fn(), get url() { return '/sessions/create'; } };
  const mockNonAdminService = { sessionInformation: { admin: false } };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormComponent],
      providers: [
        { provide: SessionService, useValue: mockNonAdminService },
        { provide: Router, useValue: mockRouter },
        { provide: TeacherService, useValue: { all: jest.fn().mockReturnValue(of([])) } },
        { provide: SessionApiService, useValue: { detail: jest.fn() } },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => null } } } },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should redirect to /sessions if user is not admin', async () => {
    await component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.sessionForm).toBeUndefined();
  });
});

describe('FormComponent Integration', () => {
  let fixture: ComponentFixture<FormComponent>;
  let component: FormComponent;
  let sessionApiService: SessionApiService;
  let routerNavigateSpy: jest.SpyInstance;

  const mockRouter = {
    navigate: jest.fn(),
    get url() { return '/sessions/create'; }
  };

  const mockActivatedRoute = {
    snapshot: { paramMap: { get: () => null } },
    root: {}
  };

  beforeEach(async () => {
    const mockSessionService = { sessionInformation: { admin: true } };
    const mockTeacherService = { all: jest.fn().mockReturnValue(of([{ id: 1, firstName: 'Jane', lastName: 'Doe' }])) };
    const mockSessionApiService = {
      create: jest.fn().mockReturnValue(of(null)),
      update: jest.fn().mockReturnValue(of(null)),
      detail: jest.fn().mockReturnValue(of({
        id: 1,
        name: 'Yoga',
        date: new Date(),
        teacherId: 1,
        description: 'Desc',
        users: []
      }))
    };

    await TestBed.configureTestingModule({
      imports: [FormComponent, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    routerNavigateSpy = jest.spyOn(mockRouter, 'navigate');
    fixture.detectChanges();
  });

  it('should create the component and load teachers', async () => {
    expect(component).toBeTruthy();
    component.teachers$.subscribe(teachers => {
      expect(teachers.length).toBe(1);
      expect(teachers[0].firstName).toBe('Jane');
    });
  });

  it('should call create session on submit', async () => {
    component.sessionForm?.setValue({
      name: 'Yoga',
      date: '2026-01-09',
      teacherId: 1,
      description: 'Desc'
    });

    await component.submit();

    expect(sessionApiService.create).toHaveBeenCalled();
    expect(routerNavigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should load session details for update', async () => {
    const mockUpdateRouter = { navigate: jest.fn(), get url() { return '/sessions/update/1'; } };
    component['router'] = mockUpdateRouter as any;

    await component.ngOnInit();

    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value.name).toBe('Yoga');
  });
});