import { userFixtures } from '../../../../common/fixtures/user.fixtures.cy.ts';

describe('Login spec', () => {

  it('Should login successfully and redirect to sessions', () => {
    cy.loginAndStoreSession(userFixtures)
    cy.url().should('include', '/sessions')
  })

  it('Should login and logout successfully', () => {
    cy.loginAndStoreSession(userFixtures)
    cy.url().should('include', '/sessions')
    cy.contains('span', 'Logout').click()
    cy.url().should('include', '/login')
  })

  it('Should display error message when login fails', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type('wrong@user.com')
    cy.get('input[formControlName=password]').type('badpassword')

    cy.get('button[type=submit]').click()

    cy.get('.error')
      .should('be.visible')
      .and('contain.text', 'An error occurred')

    cy.url().should('include', '/login')

    cy.window().then(win => {
      expect(win.localStorage.getItem('session')).to.be.null
    })
  })

  it('Should disable submit when email is missing or invalid', () => {
    cy.visit('/login')
    cy.get('input[formControlName=password]').type('test!1234')
    cy.get('button[type=submit]').should('be.disabled')
    cy.get('input[formControlName=email]').type('not-an-email')
    cy.get('button[type=submit]').should('be.disabled')
  })

  it('Should disable submit when password is missing', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type(userFixtures.username)
    cy.get('button[type=submit]').should('be.disabled')
  })
})
