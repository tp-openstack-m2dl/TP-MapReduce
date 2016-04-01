import sys
import random
import mapreduce


"""
Given a list of tokens, return a list of tuples of
titlecased (or proper noun) tokens and a count of '1'.
Also remove any leading or trailing punctuation from
each token.
"""
def Map(L):
  results = []
  for w in L:
    results.append ((w, 1))

  return results
 
 
"""
Given a (token, [(token, 1) ...]) tuple, collapse all the
count tuples from the Map operation into a single term frequency
number for this token, and return a final tuple (token, frequency).
"""
def Reduce(Mapping):
  return (Mapping[0], sum(pair[1] for pair in Mapping[1]))


class Person(object):
  """docstring for Person"""
  def __init__(self, age, nbAssur):
    super(Person, self).__init__()
    self.age = age
    self.nbAssur = nbAssur
    
if __name__ == '__main__':
 
  # Load file, stuff it into a string
  input_list = [Person(random.randint(8,110), random.randint(0,10)) for i in range(1000)]

  term_frequencies = mapreduce.MapReduce(Map, Reduce, input_list)
 
  # Sort the term frequencies in nonincreasing order
  for pair in term_frequencies:
    print pair[0], ":", pair[1]

  # Create random person
  Person(random.randint(8, 110), random.randint(0, 10));

