import { TestBed } from '@angular/core/testing';
import { HttpInterceptorFn } from '@angular/common/http';

import { TokenInterceptor } from './token.interceptor';

describe('TokenInterceptor', () => {
  const interceptor: HttpInterceptorFn = (req, next) => 
    TestBed.runInInjectionContext(() => {
      // Create a mock HttpHandler that calls the next handler function
      const handler: import('@angular/common/http').HttpHandler = {
        handle: next
      };
      return new TokenInterceptor().intercept(req, handler);
    });

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });
});
