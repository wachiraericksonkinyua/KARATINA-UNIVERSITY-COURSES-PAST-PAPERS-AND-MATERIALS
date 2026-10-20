import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Read the CSV file
df = pd.read_csv('C:/Users/Admin/Downloads/cinema.csv')

# Display basic information about the dataframe
print("DataFrame shape:", df.shape)
print("\nFirst 5 rows:")
print(df.head())
