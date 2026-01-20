describe('Register page', () => {

  beforeEach(() => {
    cy.visit('/register')
  })

  it('Should display register form correctly', () => {
    cy.get('mat-card-title').should('have.text', 'Register')

    cy.get('input[formControlName=firstName]').should('exist')
    cy.get('input[formControlName=lastName]').should('exist')
    cy.get('input[formControlName=email]').should('exist')
    cy.get('input[formControlName=password]').should('exist')

    cy.get('button[type=submit]').should('be.disabled')
  })

  it('Should register successfully and redirect to login', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {}
    }).as('register')

    cy.get('input[formControlName=firstName]').type('John')
    cy.get('input[formControlName=lastName]').type('Doe')
    cy.get('input[formControlName=email]').type('john.doe@example.com')
    cy.get('input[formControlName=password]').type('123456')

    cy.get('button[type=submit]').should('not.be.disabled').click()

    cy.wait('@register')

    cy.url().should('include', '/login')
  })

  it('Should display error message when register fails', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500
    }).as('register')

    cy.get('input[formControlName=firstName]').type('John')
    cy.get('input[formControlName=lastName]').type('Doe')
    cy.get('input[formControlName=email]').type('john.doe@example.com')
    cy.get('input[formControlName=password]').type('123456')

    cy.get('button[type=submit]').click()

    cy.wait('@register')

    cy.get('.error')
      .should('be.visible')
      .and('contain.text', 'An error occurred')

    cy.url().should('include', '/register')
  })

  it('Should not submit when form is invalid', () => {
    cy.get('button[type=submit]').should('be.disabled')

    cy.intercept('POST', '/api/auth/register').as('register')

    cy.get('button[type=submit]').click({ force: true })

    cy.wait(500)

    cy.get('@register.all').should('have.length', 0)
  })

  it('Should disable submit and show errors when fields are invalid', () => {
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=firstName]').type('John')
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=lastName]').type('Doe')
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=email]').type('john.doe@example.com')
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=password]').type('123456')
    cy.get('button[type=submit]').should('not.be.disabled')
  })
})
