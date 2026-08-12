@allure.label.epic:Portfolio_Management
@allure.label.owner:QA
Feature: Scenario for charles river balancer technical assessment

 @Smoke
  Scenario: verify account portfolio is not balanced
    Given portfolio account CRD001
    When send request to get account portfolio details
    And verify account portfolio details has been retrieved
    Then Verify account portfolio is not balanced

#Logic to calculate rebalancing recommendations
#Given account 100% vested asset $100K
#Each security has a target allocation percentage 20%
#IBM has variance of -10% so balance it, we need to find $ amount if its value which is
#Basic formula --> Stock #= variance * total assets/ share price
#$100000 * 0.20 = $20000
#$100000 * 0.10 = $10000
#$10000 * 0.20 - $10000 = $10000
#$10000 / $150 = 66 shares to buy

  @Smoke
  Scenario: verify rebalancing recommendations for account CRD001
    Given portfolio account CRD001
    When post request to get rebalancing recommendations
    Then verify the following rebalancing recommendations:
      | security | action | shares |
      | IBM      | BUY    | 66     |
      | MSFT     | HOLD   | 0      |
      | ORCL     | SELL   | 45     |
      | AAPL     | HOLD   | 0      |
      | HD       | HOLD   | 0      |


#This Automation can handles multiple ways
# 1 - Assumption due to intraday credit facility BUY and SEL can be performed in same dat as Assets are fixed at $100K and Buying may need
#     Availability of ready funds
# 2 - Manually as a Test step I would execute SELL (ORCl) first and then BUY (IBM) next day to ensure availability of funds for BUY

  @Smoke
  Scenario: Perform rebalancing recommendations for account CRD001
    Given portfolio account CRD001
    When post request to perform rebalancing recommendations
    Then Verify portfolio balancing is successful and account portfolio is balanced
      | security | target |
      | IBM      | 20     |
      | MSFT     | 20     |
      | ORCL     | 20     |
      | AAPL     | 20     |
      | HD       | 20     |

  @Smoke
  Scenario: verify account portfolio is balanced
    Given portfolio account CRD001-after
    When send request to get account portfolio details
    And verify account portfolio details has been retrieved
    Then Verify account portfolio is balanced

  @Smoke
  Scenario: verify rebalancing recommendations for account CRD001 after rebalancing
    Given portfolio account CRD001-after
    When post request to get rebalancing recommendations
    Then verify the following rebalancing recommendations:
      | security | action | shares |
      | IBM      | HOLD   | 0      |
      | MSFT     | HOLD   | 0      |
      | ORCL     | HOLD   | 0      |
      | AAPL     | HOLD   | 0      |
      | HD       | HOLD   | 0      |

  @Regression
  Scenario: verify sum of account target and current before rebalancing is 100
    Given portfolio account CRD001
    When send request to get account portfolio details
    Then verify account portfolio details has been retrieved
    And verify account portfolio target sum is 100
    And verify account portfolio current sum is 100
    And verify share price of each security is greater than 0

  @Regression
  Scenario: verify sum of account target and current after rebalancing is 100
    Given portfolio account CRD001-after
    When send request to get account portfolio details
    Then verify account portfolio details has been retrieved
    And verify account portfolio target sum is 100
    And verify account portfolio current sum is 100
    And verify share price of each security is greater than 0
