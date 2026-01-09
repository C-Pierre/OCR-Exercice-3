import { TestBed } from '@angular/core/testing';
import { TeacherService } from './teacher.service';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Teacher } from '../models/teacher.interface';
import { expect } from '@jest/globals';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.baseUrl}/teacher`;

  const mockTeacher: Teacher = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    createdAt: new Date('2026-01-09T10:00:00Z'),
    updatedAt: new Date('2026-01-09T12:00:00Z')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        TeacherService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all teachers', () => {
    const teachers: Teacher[] = [mockTeacher];

    service.all().subscribe(result => {
      expect(result).toEqual(teachers);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');

    req.flush(teachers);
  });

  it('should get teacher detail', () => {
    service.detail('1').subscribe(result => {
      expect(result).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('GET');

    req.flush(mockTeacher);
  });
});
