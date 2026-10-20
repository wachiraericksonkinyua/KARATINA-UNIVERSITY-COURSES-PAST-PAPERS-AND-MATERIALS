import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Load datasets
stores_df = pd.read_csv('Stores.csv')
airbnb_df = pd.read_csv('Airbnb_Open_Data.csv')


# i. Display first few rows
print("Stores DataFrame:")
print(stores_df.head())

# ii. Column totals
column_totals = stores_df.apply(lambda x: x.sum() if pd.api.types.is_numeric_dtype(x) else None)
stores_df.loc['Column_Totals'] = column_totals
print("\nStores DataFrame with Column Totals:")
print(stores_df.tail())

# iii. Row totals
stores_df['Row_Totals'] = stores_df.apply(lambda row: row.sum() if pd.api.types.is_numeric_dtype(row) else 0, axis=1)
print("\nStores DataFrame with Row Totals:")
print(stores_df.head())

# i. Check NaN in 'country'
nan_in_country = airbnb_df['country'].isnull().sum()
print(f"\nNumber of NaN values in 'country' column: {nan_in_country}")

# ii. Remove records with NaN in 'country'
airbnb_df_cleaned = airbnb_df.dropna(subset=['country'])
print(f"\nShape after dropping NaN in 'country': {airbnb_df_cleaned.shape}")

# iii. Find duplicates
duplicates = airbnb_df_cleaned.duplicated()
print(f"\nNumber of duplicated records: {duplicates.sum()}")

# iv. Prove duplication exists
if duplicates.any():
    print("There is at least one duplicated record.")
else:
    print("No duplicated records found.")

# v. Remove duplicates
airbnb_df_no_duplicates = airbnb_df_cleaned.drop_duplicates()
print(f"\nShape after removing duplicates: {airbnb_df_no_duplicates.shape}")

# vi. Count listings per neighborhood group
neighborhood_counts = airbnb_df_no_duplicates['neighbourhood_group'].value_counts().reset_index()
neighborhood_counts.columns = ['neighbourhood_group', 'listing_count']
print("\nListings per neighborhood group:")
print(neighborhood_counts)

# vii. Plot
plt.figure(figsize=(10,6))
sns.barplot(data=neighborhood_counts, x='neighbourhood_group', y='listing_count')
plt.title('Number of Listings in Each Neighborhood Group')
plt.xlabel('Neighborhood Group')
plt.ylabel('Number of Listings')
plt.show()

import pandas as pd
import ast
import json

# Load the dataset
df = pd.read_csv('Politics_Tweets.csv')

# Define a function to extract URLs from the media string
def extract_urls(media_str):
    try:
        
        media_list = json.loads(media_str)
        
        urls = [item['url'] for item in media_list if 'url' in item]
        return urls
    except (json.JSONDecodeError, TypeError, AttributeError):
        
        return []

# Apply the function to the 'media' column
df['media'] = df['media'].apply(extract_urls)
print(df.head())


