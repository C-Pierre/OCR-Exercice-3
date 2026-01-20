import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { NotFoundComponent } from './not-found.component';
import { By } from '@angular/platform-browser';

describe('NotFoundComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotFoundComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render main title', () => {
    const h1 = fixture.debugElement.query(By.css('h1'));
    expect(h1).toBeTruthy();
    expect(h1.nativeElement.textContent.trim()).toBe('Page not found !');
  });

  it('should render exactly one h1 element', () => {
    const titles = fixture.debugElement.queryAll(By.css('h1'));
    expect(titles.length).toBe(1);
  });

  it('should keep the same DOM after multiple change detections', () => {
    const initialHtml = fixture.nativeElement.innerHTML;

    fixture.detectChanges();
    fixture.detectChanges();
    fixture.detectChanges();

    const finalHtml = fixture.nativeElement.innerHTML;
    expect(finalHtml).toBe(initialHtml);
  });
});
