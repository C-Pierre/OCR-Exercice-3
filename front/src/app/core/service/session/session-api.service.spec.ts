import { expect } from '@jest/globals';
import { TestBed } from '@angular/core/testing';
import { Session } from '../../models/session/session.interface';
import { provideHttpClient } from '@angular/common/http';
import { SessionApiService } from './session-api.service';
import { environment } from 'src/environments/environment';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.baseUrl}/session`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SessionApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sessions', () => {
    const session: Session[] = [{
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [99, 100],
      date: new Date('2026-01-10T10:00:00Z')
    } as Session];

    service.all().subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');

    req.flush(session);
  });

  it('should get session detail', () => {
    const session: Session = {
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [99, 100],
      date: new Date('2026-01-10T10:00:00Z')
    };

    service.detail('10').subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(`${baseUrl}/10`);
    expect(req.request.method).toBe('GET');

    req.flush(session);
  });

  it('should delete a session', () => {
    service.delete('5').subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/5`);
    expect(req.request.method).toBe('DELETE');

    req.flush(null);
  });

  it('should create a session', () => {
    const session: Session = {
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [99, 100],
      date: new Date('2026-01-10T10:00:00Z')
    };

    service.create(session).subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(session);

    req.flush(session);
  });

  it('should update a session', () => {
    const session: Session = {
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [99, 100],
      date: new Date('2026-01-10T10:00:00Z')
    };

    service.update('30', session).subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(`${baseUrl}/30`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(session);

    req.flush(session);
  });

  it('should participate in session', () => {
    service.participate('1', '99').subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/participate/99`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();

    req.flush(null);
  });

  it('should unParticipate from session', () => {
    service.unParticipate('1', '99').subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/participate/99`);
    expect(req.request.method).toBe('DELETE');

    req.flush(null);
  });
});
