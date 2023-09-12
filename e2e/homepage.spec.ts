import { expect, test } from '@playwright/test'
import { testAuth } from './fixtures/authenticatedUserPages'
import { waitForInitialPageLoad } from './helpers/utils'

test.describe('Homepage', () => {
  test('should show Log In To Synapse button when logged out', async ({
    page,
  }) => {
    await page.goto('/')
    await waitForInitialPageLoad(page)

    await expect(
      page.getByRole('link', { name: 'Log in to Synapse' }),
    ).toHaveCount(2)
    await expect(
      page.getByRole('link', { name: 'View Your Dashboard' }),
    ).toHaveCount(0)
  })

  testAuth(
    'should show View Your Dashboard button when logged in',
    async ({ page }) => {
      await page.goto('/')
      await waitForInitialPageLoad(page)

      await expect(
        page.getByRole('link', { name: 'Log in to Synapse' }),
      ).toHaveCount(2)
      await expect(
        page.getByRole('link', { name: 'View Your Dashboard' }),
      ).toHaveCount(0)
    },
  )
})
