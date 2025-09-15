import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatsWsComponent } from './chats-ws.component';

describe('ChatsWsComponent', () => {
  let component: ChatsWsComponent;
  let fixture: ComponentFixture<ChatsWsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatsWsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatsWsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
