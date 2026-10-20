if 'neighbourhood group' in airbnb_df.columns:
    listings_by_group = airbnb_df.groupby('neighbourhood group').size().reset_index(name='listing_count')
else:
    raise KeyError("'neighbourhood group' column not found. Columns are: " + ", ".join(airbnb_df.columns))

print(listings_by_group.sort_values('listing_count', ascending=False))

import matplotlib.pyplot as plt
import seaborn as sns
listings_by_neighbourhood = ( airbnb_df.groupby('neighbourhood')
    .size()
    .reset_index(name='listing_count')
)
top20 = listings_by_neighbourhood.sort_values(
    'listing_count', ascending=False).head(20)
plt.figure(figsize=(14, 8))
sns.barplot(
    data=top20,
    x='listing_count',
    y='neighbourhood'
)

plt.title('Top 20 Neighbourhoods by Number of Airbnb Listings')
plt.xlabel('Number of Listings')
plt.ylabel('Neighbourhood')
plt.tight_layout()
plt.show()

