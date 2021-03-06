import sqlite3
from config import active as conf

# Menu module for puling data from the database

def query_active_menu():
	'''Get all dishes in the active menu'''

	# Parameters to be pulled from db table
	select_params = ['ItemId', 'Name', 'Price']

	# Build the database query
	query = '''
	SELECT {select_params}
	FROM {table}
	WHERE Status='{status}'
	;
	'''.format(
		select_params=','.join(select_params),
		table=conf.MENU_TABLE,
		status='Active'
	)
	
	response = execute_query(query)

	# Map database response to a dictionary using select params as keys
	response_array = response_to_array(response, select_params)
	
	return response_array


def query_id_menu(id):
	'''Get all dishes in the active menu'''

	# Parameters to be pulled from db table
	select_params = ['Name', 'Price', 'Status']

	# Build the database query
	query = '''
	SELECT {select_params}
	FROM {table}
	WHERE ItemId='{itemid}'
	;
	'''.format(
		select_params=','.join(select_params),
		table=conf.MENU_TABLE,
		itemid=id
	)

	response = execute_query(query)

	# Map database response to a dictionary using select params as keys
	response_array = response_to_array(response, select_params)

	return response_array
	
def query_all_dishes():
	'''Get all dishes from the db regardless of status'''
	
	# Init parameters to be pulled from db table
	select_params = ['ItemId', 'Name', 'Price', 'Status']

	# Build the database query
	query = '''
	SELECT {select_params}
	FROM {table}
	;
	'''.format(
		select_params=','.join(select_params),
		table=conf.MENU_TABLE
	)
	
	response = execute_query(query)

	# Map database response to an array of dictionaries using select params as keys
	response_array = response_to_array(response, select_params)
		
	return response_array

def execute_query(query):
	'''Execute a given query on the database determined by the provided config'''
	
	with sqlite3.connect(conf.DB) as conn:
		c = conn.cursor()
		c.execute(query)
		response = c.fetchall()
		
	return response
	
def response_to_array(response, select_params):
	'''Map a query response to an array of dictionaries representing the response'''
	
	response_array = []
	# For every response
	for c in range(len(response)): 
		
		dict = {}
		
		# Insert a dict of key value (column:data) pair
		for i,p in zip(range(len(select_params)), select_params): 
			dict[p] = response[c][i]
			
		response_array.append(dict)
			
	return response_array
	