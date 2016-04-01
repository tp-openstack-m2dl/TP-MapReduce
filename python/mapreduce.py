from multiprocessing import Pool

"""
A generator function for chopping up a given list into chunks of
length n.
"""
def chunks(l, n):
  for i in xrange(0, len(l), n):
    yield l[i:i+n]

def MapReduce(Map, Reduce, text):
  l = min(len(text),8)
  # Fragment the string data into 8 chunks
  partitioned_text = list(chunks(text, len(text) / l)) 
 
  # Build a pool of l processes
  pool = Pool(processes=l,)
 
  # Generate count tuples for title-cased tokens
  single_count_tuples = pool.map(Map, partitioned_text)
 
  # Organize the count tuples; lists of tuples by token key
  token_to_tuples = Partition(single_count_tuples)
 
  # Collapse the lists of tuples into total term frequencies
  term_frequencies = pool.map(Reduce, token_to_tuples.items())
  return term_frequencies

"""
Group the sublists of (token, 1) pairs into a term-frequency-list
map, so that the Reduce operation later can work on sorted
term counts. The returned result is a dictionary with the structure
{token : [(token, 1), ...] .. }
"""
def Partition(L):
  tf = {}
  for sublist in L:
    for p in sublist:
      # Append the tuple to the list in the map
      try:
        tf[p[0]].append (p)
      except KeyError:
        tf[p[0]] = [p]

  return tf




