import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

df = pd.read_csv('Education_attainment.csv', na_values=['---'])


bachelors_1980 = df[
    (df['Year'] == 1980) & 
    (df['Min degree'] == "bachelor's") & 
    (df['Sex'].isin(['F', 'M']))
]


print(bachelors_1980[['Year', 'Sex', 'Min degree', 'total']])

# Question (c):
df_decade = df[(df['Year'] >= 2000) & (df['Year'] <= 2010)]

most_common = df_decade.groupby('Min degree')['total'].mean().sort_values(ascending=False)

print("Average attainment percentage by degree (2000-2010):")
print(most_common.head(2))

# Question (d)
print("\nQuestion (d)Difference (2000 vs 2010) ")


bach_2000 = df[(df['Year'] == 2000) & (df['Min degree'] == "bachelor's") & (df['Sex'] == 'A')]['total'].values[0]
bach_2010 = df[(df['Year'] == 2010) & (df['Min degree'] == "bachelor's") & (df['Sex'] == 'A')]['total'].values[0]

diff = bach_2010 - bach_2000

print(f"Percentage in 2000: {bach_2000}%")
print(f"Percentage in 2010: {bach_2010}%")
print(f"Difference (Growth): {diff:.2f}%")


# Question (f)
sns.set_theme(style="whitegrid")

#i
#plt.figure(figsize=(10, 6))
#bach_all = df[(df['Min degree'] == "bachelor's") & (df['Sex'] == 'A')]

#sns.lineplot(data=bach_all, x='Year', y='total')
#plt.title("Total Percentage of People with Bachelor's Degrees Over Time")
#plt.ylabel("Percentage")
#plt.show()

#ii. Bar Chart
plt.figure(figsize=(8, 6))

hs_2009 = df[
    (df['Year'] == 2009) & 
    (df['Min degree'] == "high school") & 
    (df['Sex'].isin(['F', 'M', 'A']))
]

sns.barplot(data=hs_2009, x='Sex', y='total', palette='viridis')
plt.title("Percentage Completed High School by Sex (2009)") # Title as requested 
plt.ylabel("Percentage")
plt.xlabel("Sex")
plt.show()