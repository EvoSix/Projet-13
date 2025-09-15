import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './pages/authenticate/register/register.component';
import { LoginComponent } from './pages/authenticate/login/login.component';
import { authGuard } from './guards/auth.guard';
import {ChatsComponent} from './pages/chats/chats.component';
import { loggedGuard } from './guards/logged.guard';
import { ChatsWsComponent } from './pages/chats-ws/chats-ws.component';

export const routes: Routes = [
  { path: '', canActivate: [loggedGuard], component: HomeComponent },
  {
    path: 'register',
    canActivate: [loggedGuard],
    component: RegisterComponent,
  },
  { path: 'login', canActivate: [loggedGuard], component: LoginComponent },
  { path: 'chats',  component: ChatsComponent },
    { path: 'chatsWS',  component: ChatsWsComponent },
 
 
];
